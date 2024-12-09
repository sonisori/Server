ALTER TABLE `sonisori`.`sign_word_resources`
DROP FOREIGN KEY `fk_sign-word-resources_sign-word_id`;
ALTER TABLE `sonisori`.`sign_word_resources`
    ADD CONSTRAINT `fk_sign-word-resources_sign-word_id`
        FOREIGN KEY (`sign_word_id`)
            REFERENCES `sonisori`.`sign_words` (`id`)
            ON DELETE CASCADE;
