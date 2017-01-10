/*
 *  Copyright 2016 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.google.engedu.ghost;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FastDictionaryTest {

    FastDictionary dictionary;

    @Before
    public void setup()
    {
        ArrayList<String> list = new ArrayList<>();
        list.add("dance");
        list.add("done");
        list.add("math");
        list.add("mount");
        list.add("north");
        dictionary = new FastDictionary(list);
    }

    @Test
    public void testIsWord()
    {
        assertEquals(dictionary.isWord("done"), true);
        assertEquals(dictionary.isWord("dona"), false);
        assertEquals(dictionary.isWord("math"), true);
        assertEquals(dictionary.isWord("mount"), true);
        assertEquals(dictionary.isWord("norm"), false);

    }

    @Test
    public void testGetAnyWordStartingWith() {
        assertEquals(dictionary.getAnyWordStartingWith("dan"), "dance");
        assertEquals(dictionary.getAnyWordStartingWith("don"), "done");
        assertEquals(dictionary.getAnyWordStartingWith("mat"), "math");
        assertEquals(dictionary.getAnyWordStartingWith("mou"), "mount");
        assertEquals(dictionary.getAnyWordStartingWith("nor"), "north");
    }

}
