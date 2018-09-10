CREATE TABLE IF NOT EXISTS project (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    description VARCHAR(256) DEFAULT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS folder (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    project_id INT UNSIGNED NOT NULL,
    name VARCHAR(64) NOT NULL,
    description VARCHAR(256) DEFAULT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS document (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    project_id INT UNSIGNED NOT NULL,
    folder_id INT UNSIGNED NOT NULL,
    name VARCHAR(64) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    prop1 VARCHAR(256) DEFAULT NULL,
    prop2 VARCHAR(256) DEFAULT NULL,
    prop3 VARCHAR(256) DEFAULT NULL,
    prop4 VARCHAR(256) DEFAULT NULL,
    prop5 INT DEFAULT NULL,
    prop6 INT DEFAULT NULL,
    prop7 INT DEFAULT NULL,
    prop8 INT DEFAULT NULL,
    prop9 INT DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS version (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    project_id INT UNSIGNED NOT NULL,
    folder_id INT UNSIGNED NOT NULL,
    document_id INT UNSIGNED NOT NULL,
    version VARCHAR(64) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    prop1 VARCHAR(256) DEFAULT NULL,
    prop2 VARCHAR(256) DEFAULT NULL,
    prop3 VARCHAR(256) DEFAULT NULL,
    prop4 VARCHAR(256) DEFAULT NULL,
    prop5 VARCHAR(256) DEFAULT NULL,
    prop6 VARCHAR(256) DEFAULT NULL,
    prop7 INT DEFAULT NULL,
    prop8 INT DEFAULT NULL,
    prop9 INT DEFAULT NULL,
    prop10 INT DEFAULT NULL,
    prop11 INT DEFAULT NULL,
    prop12 INT DEFAULT NULL,
    prop13 INT DEFAULT NULL,
    prop14 INT DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS tag (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    description VARCHAR(256) DEFAULT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS version_tag (
    version_id INT UNSIGNED NOT NULL,
    tag_id INT UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_folder_project ON folder(project_id);
CREATE INDEX idx_document_project ON document(project_id);
CREATE INDEX idx_document_folder_name ON document(folder_id, name);
CREATE INDEX idx_version_project ON version(project_id);
CREATE INDEX idx_version_folder ON version(folder_id);
CREATE INDEX idx_version_doc_version ON version(document_id, version);
CREATE INDEX idx_tag_name ON tag(name);
CREATE INDEX idx_version_tag_both ON version_tag(version_id, tag_id);
CREATE INDEX idx_version_tag_tag ON version_tag(tag_id);