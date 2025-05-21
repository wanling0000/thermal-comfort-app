DROP TABLE IF EXISTS ai_interaction_logs;
DROP TABLE IF EXISTS comfort_summary_reports;
DROP TABLE IF EXISTS comfort_feedback;
DROP TABLE IF EXISTS environmental_readings;
DROP TABLE IF EXISTS locations;
DROP TABLE IF EXISTS activities;
DROP TABLE IF EXISTS sensors;
DROP TABLE IF EXISTS users;

-- 1. User
CREATE TABLE users (
    user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN users.user_id IS 'Primary key';
COMMENT ON COLUMN users.name IS 'Display name';
COMMENT ON COLUMN users.email IS 'Unique user email';
COMMENT ON COLUMN users.password IS 'Encrypted password';
COMMENT ON COLUMN users.created_at IS 'Registration time';

-- 2. Sensor
CREATE TABLE sensors (
    sensor_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name TEXT NOT NULL,
    type TEXT CHECK (type IN ('SwitchBot')) NOT NULL,
    last_reading_time TIMESTAMP
);
COMMENT ON COLUMN sensors.sensor_id IS 'Primary key';
COMMENT ON COLUMN sensors.name IS 'Sensor display name';
COMMENT ON COLUMN sensors.type IS 'Sensor type, e.g., "SwitchBot"';
COMMENT ON COLUMN sensors.last_reading_time IS 'Timestamp of the latest data reading';


-- 5. Activity
CREATE TABLE activities (
    activity_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name TEXT NOT NULL,
    description TEXT,
    metabolic_rate FLOAT,
    intensity_level INT CHECK (intensity_level BETWEEN 1 AND 3)
);
COMMENT ON COLUMN activities.activity_id IS 'Primary key';
COMMENT ON COLUMN activities.name IS 'Activity label, e.g., ''Studying''';
COMMENT ON COLUMN activities.description IS 'Description of the activity';
COMMENT ON COLUMN activities.metabolic_rate IS 'Optional: estimated metabolic rate';
COMMENT ON COLUMN activities.intensity_level IS 'Optional: 1=low, 2=med, 3=high';


-- 3. Location
CREATE TABLE locations (
    location_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name TEXT NOT NULL,
    description TEXT,
    is_custom BOOLEAN DEFAULT FALSE,
    custom_tag TEXT,
    coordinates GEOMETRY(Point, 4326) NOT NULL,
    user_id UUID REFERENCES users(user_id) ON DELETE CASCADE,
    UNIQUE (user_id, coordinates)
);
COMMENT ON COLUMN locations.location_id IS 'Primary key';
COMMENT ON COLUMN locations.name IS 'Display name, e.g., "Library South Wing"';
COMMENT ON COLUMN locations.description IS 'Optional details';
COMMENT ON COLUMN locations.coordinates IS 'GPS coordinates (non-unique; multiple locations can share the same)';
COMMENT ON COLUMN locations.user_id IS 'ID of the user who created or owns this location';


-- 4. EnvironmentalReading
CREATE TABLE environmental_readings (
    reading_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    timestamp TIMESTAMP NOT NULL,
    temperature FLOAT NOT NULL,
    humidity FLOAT NOT NULL,
    battery FLOAT,
    sensor_id UUID REFERENCES sensors(sensor_id) ON DELETE CASCADE,
    location_tag_id UUID NOT NULL REFERENCES locations(location_id) ON DELETE RESTRICT,
    raw_coordinates GEOMETRY(Point, 4326)
);
COMMENT ON COLUMN environmental_readings.reading_id IS 'Primary key';
COMMENT ON COLUMN environmental_readings.timestamp IS 'When the reading was taken';
COMMENT ON COLUMN environmental_readings.temperature IS 'Temperature value in Celsius';
COMMENT ON COLUMN environmental_readings.humidity IS 'Relative humidity in %';
COMMENT ON COLUMN environmental_readings.sensor_id IS 'Associated sensor';
COMMENT ON COLUMN environmental_readings.location_tag_id IS 'User-defined or default location tag';
COMMENT ON COLUMN environmental_readings.raw_coordinates IS 'Actual GPS coordinates where the reading occurred';


-- 6. ComfortFeedback
CREATE TABLE comfort_feedback (
    feedback_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(user_id) ON DELETE CASCADE,
    timestamp TIMESTAMP NOT NULL,
    comfort_level INT CHECK (comfort_level BETWEEN -2 AND 2),
    feedback_type TEXT CHECK (feedback_type IN ('quick', 'detailed')) NOT NULL DEFAULT 'detailed',
    activity_type_id UUID REFERENCES activities(activity_id),
    clothing_level TEXT CHECK (clothing_level IN ('light', 'medium', 'heavy')),
    adjusted_temp_level INT,
    adjusted_humid_level INT,
    notes TEXT,
    location_tag_id UUID NOT NULL REFERENCES locations(location_id) ON DELETE RESTRICT,
    reading_id UUID REFERENCES environmental_readings(reading_id) ON DELETE SET NULL,
    raw_coordinates GEOMETRY(Point, 4326)
);
COMMENT ON COLUMN comfort_feedback.feedback_id IS 'Primary key';
COMMENT ON COLUMN comfort_feedback.user_id IS 'Associated user';
COMMENT ON COLUMN comfort_feedback.timestamp IS 'Feedback time';
COMMENT ON COLUMN comfort_feedback.comfort_level IS 'Scale -2 (too cold) to +2 (too hot)';
COMMENT ON COLUMN comfort_feedback.feedback_type IS 'Quick vs detailed';
COMMENT ON COLUMN comfort_feedback.activity_type_id IS 'FK to Activity';
COMMENT ON COLUMN comfort_feedback.clothing_level IS 'One of "light", "medium", "heavy"';
COMMENT ON COLUMN comfort_feedback.adjusted_temp_level IS 'Desired change to temp, e.g., -1 = want cooler';
COMMENT ON COLUMN comfort_feedback.adjusted_humid_level IS 'Desired change to humidity';
COMMENT ON COLUMN comfort_feedback.notes IS 'Freeform user notes';
COMMENT ON COLUMN comfort_feedback.location_tag_id IS 'Location of feedback';
COMMENT ON COLUMN comfort_feedback.reading_id IS 'FK to associated environmental reading';
COMMENT ON COLUMN comfort_feedback.raw_coordinates IS 'Actual location when feedback was submitted';


-- 7. ComfortSummaryReport
CREATE TABLE comfort_summary_reports (
    summary_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(user_id),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    temp_comfort_range JSON,
    location_stats JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    generated_by TEXT CHECK (generated_by IN ('system', 'user_request', 'scheduled')),
    source_type TEXT CHECK (source_type IN ('backend_analysis', 'ai_generated'))
);
COMMENT ON COLUMN comfort_summary_reports.summary_id IS 'Primary key';
COMMENT ON COLUMN comfort_summary_reports.user_id IS 'Owner';
COMMENT ON COLUMN comfort_summary_reports.start_date IS 'Start date of time range analyzed';
COMMENT ON COLUMN comfort_summary_reports.end_date IS 'End date of time range analyzed';
COMMENT ON COLUMN comfort_summary_reports.temp_comfort_range IS 'Computed range';
COMMENT ON COLUMN comfort_summary_reports.location_stats IS 'Most frequent feedback per location';
COMMENT ON COLUMN comfort_summary_reports.created_at IS 'When analysis was created';
COMMENT ON COLUMN comfort_summary_reports.generated_by IS 'One of "system", "user_request", "scheduled"';
COMMENT ON COLUMN comfort_summary_reports.source_type IS 'One of "backend_analysis", "ai_generated"';

-- 8. AIInteractionLog
CREATE TABLE ai_interaction_logs (
    log_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(user_id),
    timestamp TIMESTAMP NOT NULL,
    question TEXT NOT NULL,
    response TEXT NOT NULL,
    used_summary_id UUID REFERENCES comfort_summary_reports(summary_id),
    response_type TEXT CHECK (response_type IN ('explanation', 'advice', 'suggestion')),
    used_model TEXT
);
COMMENT ON COLUMN ai_interaction_logs.log_id IS 'Primary key';
COMMENT ON COLUMN ai_interaction_logs.user_id IS 'Who sent it';
COMMENT ON COLUMN ai_interaction_logs.timestamp IS 'When asked';
COMMENT ON COLUMN ai_interaction_logs.question IS 'Prompt content';
COMMENT ON COLUMN ai_interaction_logs.response IS 'AI answer';
COMMENT ON COLUMN ai_interaction_logs.used_summary_id IS 'FK to ComfortSummaryReport, if linked';
COMMENT ON COLUMN ai_interaction_logs.response_type IS 'One of "explanation", "advice", "suggestion"';
COMMENT ON COLUMN ai_interaction_logs.used_model IS 'e.g. "gpt-4"';