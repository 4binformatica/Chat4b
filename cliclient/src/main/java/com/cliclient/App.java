package com.cliclient;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.WebSocket;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    

    public static void main( String[] args ) throws URISyntaxException
    {
        Client client = new Client(new URI("ws://87.4.163.109:8887/" ));
        

        System.out.println();
        System.out.println("                                                                                              bbbbbbbb            ");
        System.out.println("        CCCCCCCCCCCCChhhhhhh                                       tttt             444444444 b::::::b            ");
        System.out.println("     CCC::::::::::::Ch:::::h                                    ttt:::t            4::::::::4 b::::::b            ");
        System.out.println("   CC:::::::::::::::Ch:::::h                                    t:::::t           4:::::::::4 b::::::b            ");
        System.out.println("  C:::::CCCCCCCC::::Ch:::::h                                    t:::::t          4::::44::::4  b:::::b            ");
        System.out.println(" C:::::C       CCCCCC h::::h hhhhh         aaaaaaaaaaaaa  ttttttt:::::ttttttt   4::::4 4::::4  b:::::bbbbbbbbb    ");
        System.out.println("C:::::C               h::::hh:::::hhh      a::::::::::::a t:::::::::::::::::t  4::::4  4::::4  b::::::::::::::bb  ");
        System.out.println("C:::::C               h::::::::::::::hh    aaaaaaaaa:::::at:::::::::::::::::t 4::::4   4::::4  b::::::::::::::::b ");
        System.out.println("C:::::C               h:::::::hhh::::::h            a::::atttttt:::::::tttttt4::::444444::::444b:::::bbbbb:::::::b");
        System.out.println("C:::::C               h::::::h   h::::::h    aaaaaaa:::::a      t:::::t      4::::::::::::::::4b:::::b    b::::::b");
        System.out.println("C:::::C               h:::::h     h:::::h  aa::::::::::::a      t:::::t      4444444444:::::444b:::::b     b:::::b");
        System.out.println("C:::::C               h:::::h     h:::::h a::::aaaa::::::a      t:::::t                4::::4  b:::::b     b:::::b");
        System.out.println(" C:::::C       CCCCCC h:::::h     h:::::ha::::a    a:::::a      t:::::t    tttttt      4::::4  b:::::b     b:::::b");
        System.out.println("  C:::::CCCCCCCC::::C h:::::h     h:::::ha::::a    a:::::a      t::::::tttt:::::t      4::::4  b:::::bbbbbb::::::b");
        System.out.println("   CC:::::::::::::::C h:::::h     h:::::ha:::::aaaa::::::a      tt::::::::::::::t    44::::::44b::::::::::::::::b ");
        System.out.println("     CCC::::::::::::C h:::::h     h:::::h a::::::::::aa:::a       tt:::::::::::tt    4::::::::4b:::::::::::::::b  ");
        System.out.println("        CCCCCCCCCCCCC hhhhhhh     hhhhhhh  aaaaaaaaaa  aaaa         ttttttttttt      4444444444bbbbbbbbbbbbbbbb   ");
        System.out.println("                                                                                                                  ");
        System.out.println("                                                                                                                  ");
        System.out.println("                                                                                                                  ");

        client.connect();
        client.start();
    }

}
