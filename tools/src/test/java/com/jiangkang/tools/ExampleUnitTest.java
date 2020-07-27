package com.jiangkang.tools;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
  @Test public void addition_isCorrect() throws Exception {

    int a = 0x0000_0111;

    byte b = (byte) a;

    String str = "d";
    System.out.println(a);
    System.out.println(str.toCharArray());
    System.out.println(str.getBytes());

    assertEquals(4, 2 + 2);
  }


}