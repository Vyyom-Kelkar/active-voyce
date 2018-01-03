package com.example.vyyom.activevoyce.database.activevoyce;

/**
 * Created by Vyyom on 12/22/2017.
 *
 * Class contains database schema and String constants for tables and columns.
 */

public class ActiveVoyceDatabaseSchema {

    public static final class Users {
        public static final String NAME = "User";

        static final class Cols {
            static final String USER_NAME = "UserName";
            static final String PASSWORD = "Password";
            static final String HIGHSCORE = "HighScore";
            static final String CURRENTSCORE = "CurrentScore";
        }
    }

    static final class WordCombinations {
        static final String NAME = "WordCombinations";

        static final class Cols {
            static final String WORD = "Word";
            static final String VERB = "Verb";
            static final String PREPOSITION = "Preposition";
            static final String SYNONYM1 = "Synonym1";
            static final String SYNONYM2 = "Synonym2";
        }
    }

    static final class Completions {
        static final String NAME = "Completions";

        static final class Cols {
            static final String USER = "User";
            static final String WORD = "Word";
            static final String COMPLETE = "Complete";
        }
    }
}
