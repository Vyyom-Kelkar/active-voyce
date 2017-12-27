package com.example.vyyom.activevoyce;

/*
  Created by Vyyom on 12/27/2017.

  This file uses source code taken from https://github.com/teabow/android-csv-reader.
  The source code is distributed under the MIT license.

  The MIT License (MIT)

  Copyright (c) 2015 Thibaud Bourgeois

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:

  The above copyright notice and this permission notice shall be included in all
  copies or substantial portions of the Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  SOFTWARE.
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import android.content.Context;

import com.example.vyyom.activevoyce.CSVAnnotation.CSVSetter;

public class CSVReader {

    private static final String SEP = ",";

    public static <T> List<Object> readFile(Context context, int id, Class<T> cl) throws IOException, IllegalAccessException, InstantiationException, IllegalArgumentException, InvocationTargetException {

        List<Object> list = new ArrayList<>();
        List<Method> methods = new ArrayList<>();
        for (int i=0; i<cl.getDeclaredMethods().length; i++) {
            try {
                if (cl.getDeclaredMethods()[i].getName().startsWith("set")) {
                    methods.add(cl.getDeclaredMethods()[i]);
                }
            } catch (IllegalArgumentException | SecurityException e) {
                e.printStackTrace();
            }
        }
        InputStream in = context.getResources().openRawResource(id);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;
        boolean firstLine = true;
        List<String> labelsName = new ArrayList<>();
        while ((line=br.readLine())!=null) {
            T object;
            StringTokenizer st = new StringTokenizer(line, SEP);
            if (st.countTokens()>0) {
                int count = 0;
                object = cl.newInstance();
                while (st.hasMoreElements()) {
                    String elt = (String) st.nextElement();
                    if (firstLine) {
                        int indexSEP = elt.indexOf("_");
                        if (indexSEP > -1 && indexSEP<elt.length()-2) {
                            labelsName.add(elt);
                        }
                        else {
                            labelsName.add(elt);
                        }
                    }
                    else {
                        String label = labelsName.get(count++);
                        for (Method method : methods) {
                            if (method.getAnnotation(CSVSetter.class).info().equals(label)) {
                                method.invoke(object, elt);
                                break;
                            }
                        }
                    }
                }
                list.add(object);
            }
            firstLine = false;
        }
        return list.subList(1, list.size());
    }
}