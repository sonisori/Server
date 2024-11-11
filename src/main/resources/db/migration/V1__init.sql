CREATE TABLE IF NOT EXISTS `sonisori`.`users` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(20) NOT NULL,
    `email` VARCHAR(45) NOT NULL,
    `role` ENUM('ROLE_USER','ROLE_ADMIN') NOT NULL DEFAULT 'ROLE_USER',
    `username` VARCHAR(500) NOT NULL,
    `social_type` ENUM('kakao','naver', 'none') NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP(),
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE IF NOT EXISTS `sonisori`.`sign_topics` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(45) NOT NULL,
    `contents` VARCHAR(255) NOT NULL,
    `difficulty` ENUM('EASY','MEDIUM','HARD') NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP(),
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE IF NOT EXISTS `sonisori`.`sign_quizzes` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `sign_topic_id` BIGINT NOT NULL,
    `sentence` VARCHAR(255) NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP(),
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE IF NOT EXISTS `sonisori`.`quiz_histories` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `sign_topic_id` BIGINT NOT NULL,
    `count` INT NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP(),
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE IF NOT EXISTS `sonisori`.`sign_words` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `word` VARCHAR(50) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

ALTER TABLE `sonisori`.`sign_quizzes`
    ADD INDEX `fk_sign-topics_sign-quizzes_id_idx` (`sign_topic_id` ASC) VISIBLE;

ALTER TABLE `sonisori`.`sign_quizzes`
    ADD CONSTRAINT `fk_sign-topics_sign-quizzes_id`
        FOREIGN KEY (`sign_topic_id`)
            REFERENCES `sonisori`.`sign_topics` (`id`)
            ON DELETE CASCADE
            ON UPDATE NO ACTION;

ALTER TABLE `sonisori`.`quiz_histories`
    ADD INDEX `fk_quiz-histories_sign-topics_id_idx` (`sign_topic_id` ASC) VISIBLE,
    ADD INDEX `fk_quiz-histories_users_id_idx` (`user_id` ASC) VISIBLE;

ALTER TABLE `sonisori`.`quiz_histories`
    ADD CONSTRAINT `fk_quiz-histories_sign-topics_id`
        FOREIGN KEY (`sign_topic_id`)
            REFERENCES `sonisori`.`sign_topics` (`id`)
            ON DELETE CASCADE
            ON UPDATE NO ACTION,
    ADD CONSTRAINT `fk_quiz-histories_users_id`
        FOREIGN KEY (`user_id`)
            REFERENCES `sonisori`.`users` (`id`)
            ON DELETE CASCADE
            ON UPDATE NO ACTION;
