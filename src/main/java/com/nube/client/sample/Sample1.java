/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nube.client.sample;

import com.nube.core.api.Server;

/**
 *
 * @author incognito
 */
public class Sample1 extends Server {

    public Sample1(String name){
        super(name);
    }
    public void start() throws Exception {

        
        super.start();;

    }

    public static void main(String args[]) throws Exception {
        Sample1 sample = new Sample1("prueba");
        sample.start();;
    }

}
