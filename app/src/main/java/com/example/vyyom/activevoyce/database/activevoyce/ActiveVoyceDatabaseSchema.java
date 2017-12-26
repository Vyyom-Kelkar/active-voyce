package com.example.vyyom.activevoyce.database.activevoyce;

/**
 * Created by Vyyom on 12/22/2017.
 *
 * Class contains database schema and String constants for tables and columns.
 */

public class ActiveVoyceDatabaseSchema {

    public static final class Users {
        public static final String NAME = "User";

        public static final class Cols {
            public static final String USER_NAME = "UserName";
            public static final String PASSWORD = "Password";
            public static final String HIGHSCORE = "HighScore";
        }
    }

    public static final class WordCombinations {
        public static final String NAME = "WordCombinations";

        public static final class Cols {
            public static final String WORD = "Word";
            public static final String VERB = "Verb";
            public static final String PREPOSITION = "Preposition";
            public static final String SYNONYM1 = "Synonym1";
            public static final String SYNONYM2 = "Synonym2";
        }
    }
}
