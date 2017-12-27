package com.example.vyyom.activevoyce;

import android.content.Context;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by Vyyom on 12/27/2017.
 *
 * This class handles the reading of CSV input data file and saving the data to the database.
 */

public class CSVHandler {

    public List<Object> readData(Context context) throws InvocationTargetException,
            InstantiationException, IllegalAccessException, IOException {
        return CSVReader.readFile(context, R.raw.input, WordCombinations.class);
    }
}
