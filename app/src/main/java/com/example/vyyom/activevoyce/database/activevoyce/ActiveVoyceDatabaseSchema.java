package com.example.vyyom.activevoyce.database.activevoyce;

/**
 * Created by Vyyom on 12/22/2017.
 *
 * Class contains database schema and String constants for tables and columns.
 */

class ActiveVoyceDatabaseSchema {

    static final class Users {
        static final String NAME = "User";

        static final class Cols {
            static final String USER_NAME = "UserName";
            static final String PASSWORD = "Password";
            static final String HIGHSCORE = "HighScore";
        }
    }

    static final class FileSize {
        static final String NAME = "FileSize";

        static final class Cols {
            static final String FILENAME = "FileName";
            static final String SIZE = "Size";
        }
    }

    static final class Verbs {
        static final String NAME = "Verbs";

        static final class Cols {
            static final String ID = "Id";
            static final String VERB = "Verb";
        }
    }

    static final class Prepositions {
        static final String NAME = "Prepositions";

        static final class Cols {
            static final String ID = "Id";
            static final String PREPOSITION = "Preposition";
        }
    }

    static final class Words {
        static final String NAME = "Words";

        static final class Cols {
            static final String ID = "Id";
            static final String WORD = "Word";
        }
    }

    static final class Synonyms {
        static final String NAME = "Synonyms";

        static final class Cols {
            static final String ID = "Id";
            static final String SYNONYM = "Synonym";
        }
    }

    static final class WordCombinations {
        static final String NAME = "WordCombinations";

        static final class Cols {
            static final String VERBID = "VerbID";
            static final String PREPOSITIONID = "PrepositionID";
            static final String WORDID = "WordID";
        }
    }

    static final class WordSynonyms {
        static final String NAME = "WordSynonyms";

        static final class Cols {
            static final String WORDID = "WordID";
            static final String SYNONYMID = "SynonymID";
        }
    }
}
