/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

import Common.Configuration.ConfigManager;
import Common.Logging.ILogManager;
import Common.MsgBoxer.MBox;
import Common.PresetLoader.PresetLoader;
import auresearch.FactoryHolder;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JMenuItem;

/**
 *
 * @author d3vil401
 */
public class scenarioEventHandler extends AbstractAction
{
    private String _onMapName = null;
    
    public scenarioEventHandler(String _name)
    {
        super(_name);
        this._onMapName = _name;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        try {
            this.actionPerformedOn(this._onMapName, e);
            
        } catch (IOException ex) {
            Logger.getLogger(scenarioEventHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ConfigManager actionPerformedOn(String _item, ActionEvent e) throws IOException
    {
        ConfigManager _imported = PresetLoader.getConfigurationByName(_item);
        
        if (_imported != null)
        {
            FactoryHolder._configManager = _imported;
            FactoryHolder._configManager.reload();
            FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_INFORMATION, "Switched to " + _item);
            return _imported;
        } else 
            MBox.showBox("Importation of scenario went wrong, did you delete it?", "Couldn't import scenario.", MBox._MSGBOX_TYPE.TYPE_ERROR);
        
        return null;
    }
    
    public ConfigManager actionPerformedOn(JMenuItem _item, ActionEvent e) throws IOException
    {
        ConfigManager _imported = PresetLoader.getConfigurationByName(_item.getText());
        
        if (_imported != null)
        {
            FactoryHolder._configManager = _imported;
            FactoryHolder._configManager.reload();
            FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_INFORMATION, "Switched to " + _item);
            return _imported;
        } else 
            MBox.showBox("Importation of scenario went wrong, did you delete it?", "Couldn't import scenario.", MBox._MSGBOX_TYPE.TYPE_ERROR);
        
        return null;
    }
}
