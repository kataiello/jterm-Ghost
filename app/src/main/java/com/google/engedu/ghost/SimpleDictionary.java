/* Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private static final String TAG = "SampleDictionary";
    private ArrayList<String> words;
    private Random mRandom;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
        mRandom = new Random();
    }

    @VisibleForTesting
    public SimpleDictionary(ArrayList<String> words, long randomSeed) {
        this.words = words;
        mRandom = new Random(randomSeed);
    }

    /**
     * isWord determines whether the argument `word` is present in the dictionary.
     *
     *
     * @param word
     * @return true if the word is in the dictionary, false otherwise.
     */
    @Override
    public boolean isWord(String word)
    {
        //implemented with binary search
        //start at the beginning
        int lo = 0;
        //to the end
        int hi = words.size() - 1;
        while(lo <= hi)
        {
            //calculate the mid at each iteration
            int mid = lo + (hi - lo)/2;
            //if this is a word that starts with the prefix
            if(words.get(mid).equals(word))
            {
                return true;
            }
            //if the prefix is smaller
            else if(words.get(mid).compareTo(word) > 0)
            {
                hi = mid - 1;
            }
            //if the prefix is larger
            else if(words.get(mid).compareTo(word) < 0)
            {
                lo = mid + 1;
            }
        }
        //no word with the prefix has been found
        return false;
    }

    /**
     * Given a prefix, find any word in the dictionary which begins with that prefix, or return null
     * if the dictionary contains no words with that prefix.
     *
     * @param prefix
     * @return
     */
    @Override
    public String getAnyWordStartingWith(String prefix) throws NoSuchElementException {
        //Implemented using Binary Search
        //start at the beginning
        int lo = 0;
        //to the end
        int hi = words.size() - 1;
        while(lo <= hi)
        {
            //calculate the mid at each iteration
            int mid = lo + (hi - lo)/2;
            //if this is a word that starts with the prefix
            if(words.get(mid).startsWith(prefix))
            {
                return words.get(mid);
            }
            //if the prefix is smaller
            else if(words.get(mid).compareTo(prefix) > 0)
            {
                hi = mid - 1;
            }
            //if the prefix is larger
            else if(words.get(mid).compareTo(prefix) < 0)
            {
                lo = mid + 1;
            }
        }
        //no word with the prefix has been found
        return null;
    }

    /**
     * Given a prefix, find a word in the dictionary that makes a "good" ghost word starting with
     * that prefix, or null if no word in the dictionary starts with that prefix.
     *
     * What defines a "good" starter word is left to the implementer.
     *
     * @param prefix
     * @return
     */
    @Override
    public String getGoodWordStartingWith(String prefix) {
        String selected = null;
        //Implemented using Binary Search + some special magic
        //start at the beginning
        int lo = 0;
        //to the end
        int hi = words.size() - 1;
        int mid = lo + (hi - lo)/2;
        while(lo <= hi)
        {
            //calculate the mid at each iteration
            mid = lo + (hi - lo)/2;
            //if this is a word that starts with the prefix
            if(words.get(mid).equals(prefix))
            {
                break;
            }
            //if the prefix is smaller
            else if(words.get(mid).compareTo(prefix) > 0)
            {
                hi = mid - 1;
            }
            //if the prefix is larger
            else if(words.get(mid).compareTo(prefix) < 0)
            {
                lo = mid + 1;
            }

        }
        mid = lo + (hi - lo)/2;
        //probably at the first one, check

        ArrayList<String> goodWords = new ArrayList<>();
        int start = mid;
        //go through all words with this prefix in the dictionary
        while(mid < words.size())
        {
            if(!words.get(mid).startsWith(prefix))
            {
                break;
            }
            //check if it has the right number of letters (odd/even?)
            //first choice only has one more letter
            if(words.get(mid).length() - prefix.length() == 1)
            {
                //TODO: what if the player doesn't use that letter?
                return words.get(mid);
            }
            //otherwise, if it has an odd number of letters left
            else if((words.get(mid).length() - prefix.length()) % 2 == 1)
            {
                goodWords.add(words.get(mid));
            }

            //move the index up
            mid++;
        }

        //if we get to here, no word with only 1 letter was found, check if there are any with just odd letters
        if(!goodWords.isEmpty())
        {
            //just the first one cause i'm lazy
            /** TODO: do i care to change this? probably want to pick the shortest one
             * which would minimize the chance that the user could choose different letters
             * and change the course
             */
            return goodWords.get(0);
        }
        else
        {
            //if the list is empty, there are no words with an odd number of letters
            //--> sorry, gotta lose
            //return the first word
            //TODO: do i care to change this? maybe choose the longest word to mess with player
            return words.get(start);
        }

    }


    /**
     * Returns the index of the specified key in the specified array.
     *
     * @param  a the array of integers, must be sorted in ascending order
     * @param  key the search key
     * @return index of key in array {@code a} if present; {@code -1} otherwise
     */
    public static int indexOf(int[] a, int key) {
        int lo = 0;
        int hi = a.length - 1;
        while (lo <= hi) {
            // Key is in a[lo..hi] or not present.
            int mid = (lo + hi) / 2;
            if (key == a[mid]) {
                return mid;
            } else if (key < a[mid]) {
                hi = mid - 1;
            } else if (key > a[mid]) {
                lo = mid + 1;
            }
        }
        return -1;
    }

    /**
     * Returns the index of the specified key in the specified array.
     *
     * @param  a the array of integers, must be sorted in ascending order
     * @param  key the search key
     * @return index of key in array {@code a} if present; {@code -1} otherwise
     */
    public static int betterIndexOf(int[] a, int key) {
        int lo = 0;
        int hi = a.length - 1;
        while (lo <= hi) {
            // Key is in a[lo..hi] or not present.
            int mid = lo + (hi - lo) / 2;
            if (key < a[mid]) {
                hi = mid - 1;
            } else if (key > a[mid]) {
                lo = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }
}
