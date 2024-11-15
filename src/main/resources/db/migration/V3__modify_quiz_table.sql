ALTER TABLE `sonisori`.`sign_topics`
    ADD COLUMN `total_quizzes` INT NOT NULL DEFAULT 0 AFTER `contents`;

ALTER TABLE `sonisori`.`quiz_histories`
    CHANGE COLUMN `count` `correct_count` INT(11) NOT NULL DEFAULT 0 ;
