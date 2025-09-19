-- Enable necessary extensions
CREATE EXTENSION IF NOT EXISTS pgcrypto;
CREATE EXTENSION IF NOT EXISTS btree_gin;

---------------------------------------------------------------------
-- 1. CORE: Institutions, Groups, Requests
---------------------------------------------------------------------
CREATE TABLE institution_groups (
    group_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE institution_requests (
    request_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    group_id UUID NULL REFERENCES institution_groups(group_id),
    name VARCHAR(255) NOT NULL,
    owner_name VARCHAR(255),
    affiliating_board VARCHAR(255),
    contact JSONB,
    submitted_by UUID NULL,
    status VARCHAR(20) DEFAULT 'pending',
    submitted_at TIMESTAMPTZ DEFAULT now(),
    reviewed_by UUID NULL,
    reviewed_at TIMESTAMPTZ,
    review_notes TEXT
);

CREATE TABLE institutions (
    institution_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    group_id UUID NULL REFERENCES institution_groups(group_id),
    name VARCHAR(255) NOT NULL,
    address JSONB,
    affiliating_board VARCHAR(255) NOT NULL,
    owner_name VARCHAR(255),
    contact JSONB,
    created_by UUID NULL,
    affiliation_locked BOOLEAN DEFAULT TRUE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMPTZ DEFAULT now(),
    updated_at TIMESTAMPTZ DEFAULT now(),
    UNIQUE(group_id, name)
);

CREATE TABLE campuses (
    campus_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    institution_id UUID NOT NULL REFERENCES institutions(institution_id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    address JSONB,
    timezone VARCHAR(64) DEFAULT 'Asia/Kolkata',
    metadata JSONB,
    created_at TIMESTAMPTZ DEFAULT now()
);

---------------------------------------------------------------------
-- 2. ACADEMIC HIERARCHY
---------------------------------------------------------------------
CREATE TABLE programs (
    program_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    campus_id UUID NOT NULL REFERENCES campuses(campus_id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    program_code VARCHAR(20) NOT NULL,
    duration_years INT NOT NULL,
    UNIQUE(campus_id, program_code)
);

CREATE TABLE departments (
    department_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    program_id UUID NOT NULL REFERENCES programs(program_id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    department_code VARCHAR(10) NOT NULL,
    UNIQUE(program_id, department_code)
);

CREATE TABLE batches (
    batch_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    department_id UUID NOT NULL REFERENCES departments(department_id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    academic_year VARCHAR(9) NOT NULL,
    start_date DATE,
    end_date DATE,
    UNIQUE(department_id, name)
);

CREATE TABLE sections (
    section_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    batch_id UUID NOT NULL REFERENCES batches(batch_id) ON DELETE CASCADE,
    name VARCHAR(10) NOT NULL,
    UNIQUE(batch_id, name)
);

---------------------------------------------------------------------
-- 3. USERS, AUTH, AND RBAC
---------------------------------------------------------------------
CREATE TABLE users (
    user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    institution_id UUID NULL REFERENCES institutions(institution_id),
    primary_email VARCHAR(320) UNIQUE NOT NULL,
    display_name VARCHAR(255),
    phone VARCHAR(32),
    profile JSONB,
    created_at TIMESTAMPTZ DEFAULT now(),
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE auth_credentials (
    credential_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    auth_type VARCHAR(50) NOT NULL DEFAULT 'password',
    password_hash TEXT,
    last_login TIMESTAMPTZ,
    metadata JSONB,
    UNIQUE(user_id, auth_type)
);

CREATE TABLE roles (
    role_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    key_name TEXT UNIQUE NOT NULL,
    display_name VARCHAR(120),
    description TEXT
);

CREATE TABLE permissions (
    permission_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    key TEXT UNIQUE NOT NULL,
    description TEXT
);

CREATE TABLE role_permissions (
    role_id UUID NOT NULL REFERENCES roles(role_id),
    permission_id UUID NOT NULL REFERENCES permissions(permission_id),
    PRIMARY KEY(role_id, permission_id)
);

CREATE TABLE user_roles (
    user_role_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    role_id UUID NOT NULL REFERENCES roles(role_id),
    scope JSONB NOT NULL DEFAULT '{}',
    assigned_at TIMESTAMPTZ DEFAULT now(),
    UNIQUE(user_id, role_id, scope)
);

---------------------------------------------------------------------
-- 4. PEOPLE: STUDENTS & FACULTY
---------------------------------------------------------------------
CREATE TABLE students (
    student_id UUID PRIMARY KEY REFERENCES users(user_id) ON DELETE CASCADE,
    section_id UUID NOT NULL REFERENCES sections(section_id) ON DELETE CASCADE,
    roll_number VARCHAR(64) NOT NULL,
    admission_year INT,
    interests TEXT[],
    career_goal TEXT,
    UNIQUE(section_id, roll_number)
);

-- Student device registration for anti-proxy protection
CREATE TABLE student_devices (
    device_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    student_id UUID NOT NULL REFERENCES students(student_id) ON DELETE CASCADE,
    device_name VARCHAR(100),
    device_type VARCHAR(50),
    device_token TEXT UNIQUE,
    fcm_token TEXT, -- For push notifications
    device_info JSONB,
    registered_at TIMESTAMPTZ DEFAULT now(),
    last_seen TIMESTAMPTZ,
    is_active BOOLEAN DEFAULT TRUE,
    UNIQUE(student_id, device_id)
);

CREATE TABLE faculty (
    faculty_id UUID PRIMARY KEY REFERENCES users(user_id) ON DELETE CASCADE,
    department_id UUID NOT NULL REFERENCES departments(department_id) ON DELETE CASCADE,
    employee_id VARCHAR(64),
    joining_date DATE,
    metadata JSONB,
    UNIQUE(department_id, employee_id)
);

CREATE TABLE department_hods (
    hod_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    faculty_id UUID NOT NULL UNIQUE REFERENCES faculty(faculty_id) ON DELETE CASCADE,
    department_id UUID NOT NULL UNIQUE REFERENCES departments(department_id) ON DELETE CASCADE,
    assigned_at TIMESTAMPTZ DEFAULT now()
);

---------------------------------------------------------------------
-- 5. ACADEMICS: SUBJECTS, TIMETABLE, ATTENDANCE
---------------------------------------------------------------------
CREATE TABLE subjects (
    subject_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    department_id UUID NOT NULL REFERENCES departments(department_id) ON DELETE CASCADE,
    code VARCHAR(20) NOT NULL,
    name VARCHAR(255) NOT NULL,
    credits INT NOT NULL,
    UNIQUE(department_id, code)
);

CREATE TABLE terms (
    term_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    institution_id UUID NOT NULL REFERENCES institutions(institution_id) ON DELETE CASCADE,
    name VARCHAR(120) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    UNIQUE(institution_id, name)
);

CREATE TABLE curriculum (
    curriculum_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    subject_id UUID NOT NULL REFERENCES subjects(subject_id) ON DELETE CASCADE,
    section_id UUID NOT NULL REFERENCES sections(section_id) ON DELETE CASCADE,
    faculty_id UUID NOT NULL REFERENCES faculty(faculty_id),
    term_id UUID NOT NULL REFERENCES terms(term_id),
    UNIQUE(subject_id, section_id, term_id)
);

CREATE TABLE timetable_entries (
    tt_entry_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    curriculum_id UUID NOT NULL REFERENCES curriculum(curriculum_id) ON DELETE CASCADE,
    day_of_week INT NOT NULL CHECK (day_of_week BETWEEN 1 AND 7),
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    room_number VARCHAR(50),
    is_free_period BOOLEAN DEFAULT FALSE,
    status VARCHAR(20) DEFAULT 'draft' CHECK (status IN ('draft', 'published', 'archived')),
    CONSTRAINT end_after_start CHECK (end_time > start_time)
);

CREATE TABLE classrooms (
    classroom_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    campus_id UUID NOT NULL REFERENCES campuses(campus_id),
    building VARCHAR(255),
    room VARCHAR(64),
    capacity INT,
    metadata JSONB,
    UNIQUE(campus_id, building, room)
);

-- Bluetooth/Wi-Fi beacons for location verification
CREATE TABLE location_beacons (
    beacon_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    classroom_id UUID NOT NULL REFERENCES classrooms(classroom_id),
    beacon_type VARCHAR(20) NOT NULL, -- 'bluetooth', 'wifi'
    beacon_identifier VARCHAR(255) NOT NULL, -- MAC address or SSID
    beacon_name VARCHAR(100),
    is_active BOOLEAN DEFAULT TRUE,
    UNIQUE(classroom_id, beacon_identifier)
);

CREATE TABLE class_sessions (
    session_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tt_entry_id UUID NULL REFERENCES timetable_entries(tt_entry_id) ON DELETE SET NULL,
    curriculum_id UUID NOT NULL REFERENCES curriculum(curriculum_id) ON DELETE CASCADE,
    session_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    classroom_id UUID REFERENCES classrooms(classroom_id),
    status VARCHAR(20) DEFAULT 'scheduled' CHECK (status IN ('scheduled', 'completed', 'cancelled')),
    qr_token VARCHAR(128) UNIQUE,
    qr_expires_at TIMESTAMPTZ,
    UNIQUE(curriculum_id, session_date),
    CONSTRAINT end_after_start CHECK (end_time > start_time)
);

-- Secure QR code tracking
CREATE TABLE qr_code_usage (
    usage_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    qr_token VARCHAR(128) NOT NULL,
    session_id UUID NOT NULL REFERENCES class_sessions(session_id),
    faculty_id UUID NOT NULL REFERENCES users(user_id),
    generated_at TIMESTAMPTZ DEFAULT now(),
    expires_at TIMESTAMPTZ NOT NULL,
    used BOOLEAN DEFAULT FALSE,
    used_at TIMESTAMPTZ,
    used_by_device_id UUID REFERENCES student_devices(device_id)
);

CREATE TABLE attendance_records (
    attendance_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    session_id UUID NOT NULL REFERENCES class_sessions(session_id) ON DELETE CASCADE,
    student_id UUID NOT NULL REFERENCES students(student_id) ON DELETE CASCADE,
    device_id UUID REFERENCES student_devices(device_id),
    status VARCHAR(20) NOT NULL DEFAULT 'absent' CHECK (status IN ('present', 'absent', 'late', 'excused')),
    marked_at TIMESTAMPTZ DEFAULT now(),
    method VARCHAR(20) NOT NULL CHECK (method IN ('qr_scan', 'manual_by_faculty', 'beacon_auto')),
    marked_by UUID NULL REFERENCES users(user_id),
    location_data JSONB, -- {latitude: x, longitude: y, accuracy: z}
    wifi_bssid VARCHAR(100),
    bluetooth_beacon_id VARCHAR(100),
    confidence_score INT DEFAULT 100, -- 0-100 score of attendance validity
    UNIQUE(session_id, student_id)
);

-- Faculty attendance tracking
CREATE TABLE faculty_attendance (
    faculty_attendance_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    faculty_id UUID NOT NULL REFERENCES faculty(faculty_id),
    session_id UUID NOT NULL UNIQUE REFERENCES class_sessions(session_id),
    status VARCHAR(20) NOT NULL CHECK (status IN ('present', 'absent')),
    marked_at TIMESTAMPTZ DEFAULT now(),
    device_id UUID REFERENCES student_devices(device_id)
);

---------------------------------------------------------------------
-- 6. SMART FEATURES & RECOMMENDATION ENGINE
---------------------------------------------------------------------
CREATE TABLE smart_activity_types (
    type_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    icon VARCHAR(50)
);

CREATE TABLE smart_activities (
    activity_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    type_id UUID NOT NULL REFERENCES smart_activity_types(type_id),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    url TEXT NOT NULL,
    expected_duration_min INT NOT NULL,
    difficulty_level VARCHAR(20),
    tags TEXT[] NOT NULL DEFAULT '{}',
    associated_subject_id UUID REFERENCES subjects(subject_id),
    created_by UUID REFERENCES users(user_id),
    created_at TIMESTAMPTZ DEFAULT now(),
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE smart_recommendations (
    recommendation_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    student_id UUID NOT NULL REFERENCES students(student_id) ON DELETE CASCADE,
    activity_id UUID NOT NULL REFERENCES smart_activities(activity_id),
    recommended_for TIMESTAMPTZ NOT NULL,
    recommended_basis TEXT, -- 'interest_ai', 'career_goal_ds', 'weak_topic_math'
    priority INT DEFAULT 1, -- 1-5 priority level
    is_completed BOOLEAN DEFAULT FALSE,
    completed_at TIMESTAMPTZ,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    feedback TEXT,
    created_at TIMESTAMPTZ DEFAULT now()
);

-- Student learning analytics
CREATE TABLE student_learning_analytics (
    analytics_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    student_id UUID NOT NULL REFERENCES students(student_id) ON DELETE CASCADE,
    subject_id UUID REFERENCES subjects(subject_id),
    date DATE NOT NULL,
    time_spent_min INT DEFAULT 0,
    activities_completed INT DEFAULT 0,
    confidence_score INT DEFAULT 0, -- 0-100
    weak_topics TEXT[],
    strong_topics TEXT[],
    UNIQUE(student_id, subject_id, date)
);

---------------------------------------------------------------------
-- 7. ASSESSMENTS & GRADING
---------------------------------------------------------------------
CREATE TABLE assessments (
    assessment_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    section_id UUID NOT NULL REFERENCES sections(section_id),
    subject_id UUID NOT NULL REFERENCES subjects(subject_id),
    title VARCHAR(255),
    description TEXT,
    max_marks NUMERIC(5,2),
    weightage NUMERIC(5,2), -- Percentage weight in final grade
    date_conducted DATE,
    created_by UUID REFERENCES users(user_id),
    metadata JSONB
);

CREATE TABLE assessment_scores (
    score_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    assessment_id UUID NOT NULL REFERENCES assessments(assessment_id) ON DELETE CASCADE,
    student_id UUID NOT NULL REFERENCES students(student_id) ON DELETE CASCADE,
    marks_obtained NUMERIC(5,2),
    graded_by UUID REFERENCES users(user_id),
    graded_at TIMESTAMPTZ DEFAULT now(),
    feedback TEXT,
    UNIQUE(assessment_id, student_id)
);

---------------------------------------------------------------------
-- 8. EVENTS & NOTIFICATIONS
---------------------------------------------------------------------
CREATE TABLE events (
    event_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    department_id UUID NOT NULL REFERENCES departments(department_id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    start_at TIMESTAMPTZ NOT NULL,
    end_at TIMESTAMPTZ NOT NULL,
    created_by UUID NOT NULL REFERENCES users(user_id),
    visibility VARCHAR(20) DEFAULT 'department',
    status VARCHAR(20) DEFAULT 'scheduled',
    metadata JSONB
);

CREATE TABLE notifications (
    notification_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(user_id),
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    type VARCHAR(50) NOT NULL, -- 'attendance', 'assessment', 'event', 'system'
    related_id UUID, -- ID of related entity
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMPTZ DEFAULT now(),
    expires_at TIMESTAMPTZ
);

---------------------------------------------------------------------
-- 9. AUDIT LOGS & SECURITY
---------------------------------------------------------------------
CREATE TABLE audit_logs (
    log_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NULL REFERENCES users(user_id),
    action VARCHAR(255) NOT NULL,
    target_type VARCHAR(120),
    target_id UUID,
    ip_address VARCHAR(45),
    user_agent TEXT,
    metadata JSONB,
    created_at TIMESTAMPTZ DEFAULT now()
);

-- Fraud detection and anomaly tracking
CREATE TABLE security_anomalies (
    anomaly_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    student_id UUID REFERENCES students(student_id),
    faculty_id UUID REFERENCES faculty(faculty_id),
    type VARCHAR(50) NOT NULL, -- 'proxy_attempt', 'multiple_devices', 'location_mismatch'
    description TEXT NOT NULL,
    severity VARCHAR(20) DEFAULT 'medium', -- 'low', 'medium', 'high', 'critical'
    evidence JSONB,
    resolved BOOLEAN DEFAULT FALSE,
    resolved_by UUID REFERENCES users(user_id),
    resolved_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ DEFAULT now()
);

---------------------------------------------------------------------
-- 10. COUNSELING & SUPPORT
---------------------------------------------------------------------
CREATE TABLE counseling_requests (
    request_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    student_id UUID NOT NULL REFERENCES students(student_id),
    counselor_id UUID NULL REFERENCES users(user_id),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(20) DEFAULT 'open',
    priority VARCHAR(20) DEFAULT 'medium',
    requested_at TIMESTAMPTZ DEFAULT now(),
    resolved_at TIMESTAMPTZ,
    metadata JSONB
);

CREATE TABLE counseling_sessions (
    session_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    request_id UUID NOT NULL REFERENCES counseling_requests(request_id),
    conducted_by UUID NOT NULL REFERENCES users(user_id),
    session_date TIMESTAMPTZ NOT NULL,
    duration_min INT,
    notes TEXT,
    follow_up_required BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMPTZ DEFAULT now()
);

---------------------------------------------------------------------
-- INDEXES FOR PERFORMANCE
---------------------------------------------------------------------
-- Users and Authentication
CREATE INDEX idx_users_institution_id ON users(institution_id);
CREATE INDEX idx_users_email ON users(primary_email);
CREATE INDEX idx_users_is_active ON users(is_active) WHERE is_active = TRUE;

-- Students
CREATE INDEX idx_students_section_id ON students(section_id);
CREATE INDEX idx_students_roll_number ON students(roll_number);
CREATE INDEX idx_students_interests ON students USING GIN (interests);

-- Student Devices (Anti-proxy)
CREATE INDEX idx_student_devices_student_id ON student_devices(student_id);
CREATE INDEX idx_student_devices_device_token ON student_devices(device_token);
CREATE INDEX idx_student_devices_is_active ON student_devices(is_active) WHERE is_active = TRUE;

-- Attendance
CREATE INDEX idx_attendance_session_id ON attendance_records(session_id);
CREATE INDEX idx_attendance_student_id ON attendance_records(student_id);
CREATE INDEX idx_attendance_marked_at ON attendance_records(marked_at);
CREATE INDEX idx_attendance_device_id ON attendance_records(device_id);
CREATE INDEX idx_attendance_confidence ON attendance_records(confidence_score) WHERE confidence_score < 80;

-- QR Code Security
CREATE INDEX idx_qr_code_usage_token ON qr_code_usage(qr_token);
CREATE INDEX idx_qr_code_usage_session ON qr_code_usage(session_id);
CREATE INDEX idx_qr_code_usage_expires ON qr_code_usage(expires_at) WHERE used = FALSE;
CREATE UNIQUE INDEX idx_qr_code_usage_unique ON qr_code_usage(qr_token) WHERE used = FALSE;

-- Class Sessions
CREATE INDEX idx_class_sessions_date ON class_sessions(session_date);
CREATE INDEX idx_class_sessions_curriculum ON class_sessions(curriculum_id);
CREATE INDEX idx_class_sessions_qr_token ON class_sessions(qr_token) WHERE qr_token IS NOT NULL;

-- Smart Recommendations
CREATE INDEX idx_recommendations_student ON smart_recommendations(student_id);
CREATE INDEX idx_recommendations_date ON smart_recommendations(recommended_for);
CREATE INDEX idx_recommendations_completed ON smart_recommendations(is_completed) WHERE is_completed = FALSE;

-- Location Beacons
CREATE INDEX idx_beacons_classroom ON location_beacons(classroom_id);
CREATE INDEX idx_beacons_identifier ON location_beacons(beacon_identifier);
CREATE INDEX idx_beacons_active ON location_beacons(is_active) WHERE is_active = TRUE;

-- Learning Analytics
CREATE INDEX idx_analytics_student_date ON student_learning_analytics(student_id, date);
CREATE INDEX idx_analytics_subject ON student_learning_analytics(subject_id);

-- Notifications
CREATE INDEX idx_notifications_user ON notifications(user_id);
CREATE INDEX idx_notifications_unread ON notifications(is_read) WHERE is_read = FALSE;

-- Security
CREATE INDEX idx_audit_logs_user ON audit_logs(user_id);
CREATE INDEX idx_audit_logs_created ON audit_logs(created_at);
CREATE INDEX idx_anomalies_student ON security_anomalies(student_id);
CREATE INDEX idx_anomalies_unresolved ON security_anomalies(resolved) WHERE resolved = FALSE;

---------------------------------------------------------------------
-- TRIGGERS FOR DATA INTEGRITY
---------------------------------------------------------------------
-- Update timestamp trigger
CREATE OR REPLACE FUNCTION update_modified_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_institutions_updated
    BEFORE UPDATE ON institutions
    FOR EACH ROW
    EXECUTE FUNCTION update_modified_column();

-- Prevent proxy attendance trigger
CREATE OR REPLACE FUNCTION check_attendance_fraud()
RETURNS TRIGGER AS $$
BEGIN
    -- Check if device is registered to student
    IF NEW.device_id IS NOT NULL AND NOT EXISTS (
        SELECT 1 FROM student_devices 
        WHERE device_id = NEW.device_id AND student_id = NEW.student_id
    ) THEN
        INSERT INTO security_anomalies (student_id, type, description, severity)
        VALUES (NEW.student_id, 'device_mismatch', 
                'Attendance marked with device not registered to student', 'high');
    END IF;

    -- Check for multiple devices for same student in short time
    IF EXISTS (
        SELECT 1 FROM attendance_records ar
        JOIN student_devices sd ON ar.device_id = sd.device_id
        WHERE ar.student_id = NEW.student_id
        AND ar.marked_at > NOW() - INTERVAL '1 hour'
        AND sd.device_id != NEW.device_id
    ) THEN
        NEW.confidence_score = GREATEST(NEW.confidence_score - 30, 0);
        
        INSERT INTO security_anomalies (student_id, type, description, severity)
        VALUES (NEW.student_id, 'multiple_devices', 
                'Student using multiple devices in short timeframe', 'medium');
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_prevent_attendance_fraud
    BEFORE INSERT ON attendance_records
    FOR EACH ROW
    EXECUTE FUNCTION check_attendance_fraud();

-- QR code single-use enforcement
CREATE OR REPLACE FUNCTION enforce_qr_single_use()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.used = TRUE THEN
        -- Mark all other usage of this QR code as used to prevent reuse
        UPDATE qr_code_usage 
        SET used = TRUE, used_at = NOW(), used_by_device_id = NEW.used_by_device_id
        WHERE qr_token = NEW.qr_token AND used = FALSE;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_enforce_qr_single_use
    AFTER UPDATE OF used ON qr_code_usage
    FOR EACH ROW
    WHEN (NEW.used = TRUE)
    EXECUTE FUNCTION enforce_qr_single_use();

---------------------------------------------------------------------
-- INITIAL DATA
---------------------------------------------------------------------
-- Insert default roles
INSERT INTO roles (role_id, key_name, display_name, description) VALUES
('11111111-1111-1111-1111-111111111111', 'system_admin', 'System Administrator', 'Full system access'),
('22222222-2222-2222-2222-222222222222', 'institution_admin', 'Institution Administrator', 'Manages an entire institution'),
('33333333-3333-3333-3333-333333333333', 'hod', 'Head of Department', 'Manages a single department'),
('44444444-4444-4444-4444-444444444444', 'faculty', 'Faculty Member', 'Teaches subjects and manages classes'),
('55555555-5555-5555-5555-555555555555', 'student', 'Student', 'Attends classes and receives recommendations'),
('66666666-6666-6666-6666-666666666666', 'counselor', 'Career Counselor', 'Provides student guidance and support')
ON CONFLICT (key_name) DO NOTHING;

-- Insert smart activity types
INSERT INTO smart_activity_types (type_id, name, description, icon) VALUES
('77777777-7777-7777-7777-777777777777', 'Video Tutorial', 'Educational video content', 'video'),
('88888888-8888-8888-8888-888888888888', 'Practice Problem Set', 'Practice questions and exercises', 'assignment'),
('99999999-9999-9999-9999-999999999999', 'Reading Article', 'Educational articles and readings', 'article'),
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Project Task', 'Hands-on project work', 'project'),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Career Research', 'Career exploration and research', 'career'),
('cccccccc-cccc-cccc-cccc-cccccccccccc', 'Skill Assessment', 'Skill evaluation tests', 'assessment')
ON CONFLICT (name) DO NOTHING;

---------------------------------------------------------------------
-- COMMENTS ON TABLES
---------------------------------------------------------------------
COMMENT ON TABLE student_devices IS 'Stores registered student devices for anti-proxy attendance protection';
COMMENT ON TABLE qr_code_usage IS 'Tracks QR code generation and usage for secure attendance marking';
COMMENT ON TABLE location_beacons IS 'Bluetooth/Wi-Fi beacons for classroom location verification';
COMMENT ON TABLE security_anomalies IS 'Records security anomalies and potential fraud attempts';
COMMENT ON TABLE attendance_records IS 'Stores attendance records with anti-fraud metadata and confidence scoring';

-- Create database comments for important columns
COMMENT ON COLUMN attendance_records.confidence_score IS '0-100 score indicating confidence in attendance validity (lower = more suspicious)';
COMMENT ON COLUMN attendance_records.location_data IS 'GPS coordinates and accuracy data for location verification';
COMMENT ON COLUMN student_devices.device_token IS 'Unique device identifier for anti-proxy protection';
COMMENT ON COLUMN qr_code_usage.expires_at IS 'QR code expiration time (typically 2-5 minutes after generation)';