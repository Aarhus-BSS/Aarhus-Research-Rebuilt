/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PictureManipulation;

import Common.Logging.ILogManager;
import auresearch.FactoryHolder;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author d3vil401
 */
public class PictureMerger 
{
    private ArrayList<BufferedImage> _pictures = new ArrayList<>();
    private BufferedImage            _finalPicture = null;
    
    public PictureMerger()
    {
        
    }
    
    public void addPicture(BufferedImage _imageBuffer)
    {
        _pictures.add(_imageBuffer);
    }
    
    public void addPictures(ArrayList<BufferedImage> _imageBuffers)
    {
        for (int i = 0; i < _imageBuffers.size(); i++)
            _pictures.add(_imageBuffers.get(i));
    }
    
    public void addFinalPicture(BufferedImage _imageBuffer)
    {
        _finalPicture = _imageBuffer;
    }
    
    public void clean()
    {
        _pictures.clear();
        _finalPicture = null;
    }
    
    public BufferedImage render()
    {
        float _opacity = FactoryHolder._configManager.getFloatValue("MERGING_ALPHA");
        Graphics2D g = null;
        BufferedImage _render = new BufferedImage(_pictures.get(0).getWidth(), _pictures.get(0).getHeight(), BufferedImage.TYPE_INT_RGB);
        
        for (int i = 0; i < _pictures.size(); i++)
        {
            g = _pictures.get(i).createGraphics();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, _opacity));
            g.drawImage(_render, null, 0, 0);
        }
        
        if (this._finalPicture != null)
        {
            g = _finalPicture.createGraphics();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        } else
            FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "Final picture not set, you've lost the average final picture of first layer.");
        
        g.drawImage(_render, null, 0, 0);
        
        return _render;
    }
}
