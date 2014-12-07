/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nube.client.sample;

import com.nube.core.annotations.MicroService;

/**
 *
 * @author incognito
 */
@MicroService(path = "/test", port = 9000)
public class Sample1 {

    public String getNubeData(String input, String input2) {
        System.out.println("Input 1" + input);
        System.out.println("INput 2" + input2);
        return "hello world my first server";
    }
}
