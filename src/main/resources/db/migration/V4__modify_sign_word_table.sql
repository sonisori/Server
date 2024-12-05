ALTER TABLE `sonisori`.`sign_words`
    ADD COLUMN `description` VARCHAR(500) NULL AFTER `word`,
    ADD COLUMN `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP() AFTER `description`,
    ADD COLUMN `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP() AFTER `created_at`;

CREATE TABLE `sonisori`.`sign_word_resources` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `sign_word_id` BIGINT NOT NULL,
  `resource_type` ENUM('image', 'video') NOT NULL,
  `resource_url` VARCHAR(500) NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

ALTER TABLE `sonisori`.`sign_word_resources`
    ADD INDEX `fk_sign-word-resources_sign-word_id_idx` (`sign_word_id` ASC) VISIBLE;

ALTER TABLE `sonisori`.`sign_word_resources`
    ADD CONSTRAINT `fk_sign-word-resources_sign-word_id`
        FOREIGN KEY (`sign_word_id`)
            REFERENCES `sonisori`.`sign_words` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;
