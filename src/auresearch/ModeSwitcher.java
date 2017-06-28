/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auresearch;

import Common.Logging.ILogManager;
import Common.PresetLoader.PresetLoader;
import Environment.graphStatsExport;
import Environment.roundStatsExport;
import Environment.roundStatsHolder;
import Graphics.MainMenu;
import Multithreading.MRThreading;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import org.jfree.chart.ChartUtilities;

/**
 *
 * @author d3vil401
 */
public class ModeSwitcher 
{
    // There are 3 different modes, as we already discussed during the meeting.
    public enum Mode
    {
        MODE_LIBRARY("library"), MODE_SERVER("server"), MODE_CONSOLE("console");
        
        private String _mode;
        
        private Mode(final String _mode) 
        {
            this._mode = _mode;
        }
        
        @Override
        public String toString() 
        {
            return this._mode;
        }
    }
    
    // Checks which mode has been specified inside the configuration file, loweing the cases for a better general comparation.
    public static void start(String _mode)
    {
        if (_mode.toLowerCase().equals(Mode.MODE_LIBRARY.toString()))
            _startAsLibrary();
        else if (_mode.toLowerCase().equals(Mode.MODE_SERVER.toString()))
            _startAsServer();
        else if (_mode.toLowerCase().equals(Mode.MODE_CONSOLE.toString()))
            _startAsConsole();
        else
            FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_CRITICAL, "Unknown mode " + _mode);
    }
    
    // Start the simulator as "library" (GUI Application)
    private static void _startAsLibrary()
    {
        PresetLoader.assignOriginal(FactoryHolder._configManager);
        FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_NORMAL, "Loading scenario presets...");
        if (!PresetLoader.loadList())
            FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "Something went wrong by loading scenarios...");
        
        
        FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_NORMAL, "Opening main form...");
        java.awt.EventQueue.invokeLater(new Runnable() 
        {
            public void run() 
            {
                MainMenu dialog = new MainMenu(new javax.swing.JFrame(), true);
                dialog.setLocationRelativeTo(null);

                dialog.addWindowListener(new java.awt.event.WindowAdapter() 
                {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) 
                    {
                        FactoryHolder.destroy();
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    
    private static void saveChart(BufferedImage img, String name) throws IOException 
    {
        File target = new File("C:\\Aarhus University\\MergedGraphs", name);
        ByteArrayOutputStream imgOutput = new ByteArrayOutputStream();
        ChartUtilities.writeBufferedImageAsPNG(imgOutput, img);
        OutputStream out = new FileOutputStream(target);
        out.write(imgOutput.toByteArray());
        out.close();
    }
    
    // As console, the one we said we just start and outputs the graphics via the bash script.
    private static void _startAsConsole()
    {
        FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_NORMAL, "Console mode has been requested, prepare your CPU!");
        
        int _masterRuns = FactoryHolder._configManager.getNumberValue("MASTER_ITERATIONS");
        
        FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_INFORMATION, _masterRuns + " master runs requested.");
        FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_INFORMATION, "We're going to execute a total of " + _masterRuns * FactoryHolder._configManager.getNumberValue("MAX_ROUNDS") + " rounds.");
        
        int _endedThreads = 0;
        ArrayList<MRThreading> _threads = new ArrayList<>();
        int _totalSAgents = 0;
        int _totalPAgents = 0;
        
       
        for (int i = 0; i < (_masterRuns / 5); i++) {
            FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_NORMAL, "Setting up " + i + " thread...");
            _threads.add(new MRThreading());
            
        }
        
        for (int i = 0; i < _threads.size(); i++) 
        {
            _totalSAgents += _threads.get(i).getTotalSAgents();
            _totalPAgents += _threads.get(i).getTotalPAgents();
        }
        
        
        FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_INFORMATION, "Totally:\n\tSolver Agents: " + _totalSAgents + "\n\tProposer Agents: " + _totalPAgents);
        FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_NORMAL, "Running " + _threads.size() + " threads!");
        
        for (int i = 0; i < _threads.size(); i++) {
            Thread _slave = new Thread(_threads.get(i));
            _slave.setName("MR#" + i);
            _slave.start();
        }
        
        while (true)
        {
            for (int i = 0; i < _threads.size(); i++)
                if (_threads.get(i).ended())
                    _endedThreads++;
                else
                    FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_INFORMATION, "Thread MR#" + i + " is working on simulation " + (_threads.get(i).currentTurn() + 1) + " / 5.");
            
            if (_endedThreads >= (_masterRuns / 5))
                break;
            
            FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_NORMAL, "Running (" + _endedThreads + " / " + _threads.size() + ").");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException ex) {
                FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "Main thread giving problems...this is hilarious: " + ex.getMessage());
            }
        }
        
        FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_NORMAL, "All threads ended simulations, rendering graphics...");
        
        // Picture comes out completely black, to fix.
        /*
        try {
            saveChart(FactoryHolder._agentsCount.render(), "test1.png");
        } catch (IOException ex) {
            FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "Picture merge failed: " + ex.getMessage());
        }
        */
        
        ArrayList<roundStatsHolder> _stats = new ArrayList();
        for (int i = 0; i < _threads.size(); i++)
            for (int k = 0; k < 5; k++)
                _stats.add(_threads.get(i).getStatistics().get(k));
        
        roundStatsHolder _avg = new roundStatsHolder();
        ArrayList<roundStatsHolder> _localAvg = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<roundStatsHolder>>> _localAvgs = new ArrayList<>();
        // Thread number --> Simulation number --> Round number; good luck.
        
        // Logics is: we have the global average for some of the data, now we need to gather and calculate average of what is going on for each round of parallel simulation.
        //            The amount of numbers we have is huge, probably I should have rewritten the statistics system time ago.
        
        for (int i = 0; i < _threads.size(); i++)
            _localAvgs.add(_threads.get(i).getAllLocalStats());
        
        roundStatsExport.parseLocalStatsForGlobal(_localAvgs, _localAvg);
        roundStatsExport.parseGlobalStatsForGlobal(_stats, _avg);
        
        graphStatsExport.renderGraphs(FactoryHolder._configManager.getNumberValue("MAX_ROUNDS"), _avg, _localAvg);
    }
    
    // As HTTP server, not going to be developed for a while.
    private static void _startAsServer()
    {
        FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_CRITICAL, "Mode server has not been implemented yet.");
    }
}
