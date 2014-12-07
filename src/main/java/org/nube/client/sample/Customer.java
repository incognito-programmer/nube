/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nube.client.sample;

import org.nube.core.annotations.MicroService;

/**
 *
 * @author incognito
 */
@MicroService(path = "/cust", port = 8000)
public class Customer {

    public String getNubeData(String input, String input2) {
        return "hello world my first server";
    }
}
