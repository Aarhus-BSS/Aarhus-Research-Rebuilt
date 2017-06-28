/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auresearch;

import Common.Configuration.ConfigManager;
import Common.Logging.ILogManager;
import Common.Logging.cLogConsole;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author d3vil401
 */

/*

    To new developers:
    
    I know I know, code looks like a mess, but you clearly didn't see the old one.
    This work has been done between October 2016 and June 2017, with the rush of the "Milan Paper" Mission.

    It might look like a nightmare of stuff, but concept is easy to understand, the problem
    comes when you have to implement it: lots of structures and game-changin mini-details (check these at least 30 times).
    Remember simulation has to reflect reality of human behaviour, in its limits ofc, not to produce
    happy looking results only, despite wrong -> means something in the code is wrong! but if you lost at least 1 month on it and still has same results
    even after rewriting it, then Borge will be happy to see it.

    Now, what else do I have to tell you?
    Yep, one thing: don't be scared, this object-oriented spaghetti code is going to amaze you.

    Personal help about my code: 240068@via.dk (until 2018) or d3v1l401@d3vsite.org (check if website is still active, but it will be for a long time as it always has been).
    Please contact only if strictly needed, I'm probably working or something.

    Public key: B49EAC7F4C00954EEF491B79222A698411E7D47D (0x222A698411E7D47D on MITM PGP server)

    Good luck.

*/
public class AUResearch 
{   
    public static void main(String[] args) throws IOException 
    {
        // First of all it is needed to register the logging system.
        FactoryHolder._logManager = new cLogConsole();
        FactoryHolder._logManager.initializeSession("session");
        
        if (args.length > 0) {
            if (args[0].compareToIgnoreCase("-editor") == 0)
            {
                //FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_INFORMATION, "Configuration editor has been requested, firing up!");
                //java.awt.EventQueue.invokeLater(new Runnable() 
                //{
                //    public void run() 
                //    {
                //       
                //    }
                //});
            }
        } else {
            // We import the configuration stuff, it is fundamental to have this.
            FactoryHolder._configManager = new ConfigManager();
            if (FactoryHolder._configManager.hasError())
                return;

            FactoryHolder._logManager.setDebugMode(false);

            // Override simulator initialization and function to the ModeSwitcher.
            ModeSwitcher.start(FactoryHolder._configManager.getStringValue("MODE"));
        }
        
        FactoryHolder.destroy();
    }
}
