/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 * I've found this amazing code on StackoverFlow!!!!
 * here is the link:
 * 
 * http://stackoverflow.com/questions/4443878/redirecting-system-out-to-jtextpane
  */
import java.io.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

public class ConsoleTextArea implements Runnable
{
    JTextArea displayPane;
    BufferedReader reader;

    private ConsoleTextArea(JTextArea displayPane, PipedOutputStream pos)
    {
        this.displayPane = displayPane;

        try
        {
            PipedInputStream pis = new PipedInputStream( pos );
            reader = new BufferedReader( new InputStreamReader(pis) );
        }
        catch(IOException e) {}
    }

    public void run()
    {
        String line = null;

        try
        {
            while ((line = reader.readLine()) != null)
            {
//              displayPane.replaceSelection( line + "\n" );
                displayPane.append( line + "\n" );
                displayPane.setCaretPosition( displayPane.getDocument().getLength() );
            }

            System.err.println("im here");
        }
        catch (IOException ioe)
        {
//            JOptionPane.showMessageDialog(null,
//                "Error redirecting output : "+ioe.getMessage());
                redirectOut(displayPane);
        }
    }

    public static void redirectOutput(JTextArea displayPane)
    {
        ConsoleTextArea.redirectOut(displayPane);
        ConsoleTextArea.redirectErr(displayPane);
    }

    public static void redirectOut(JTextArea displayPane)
    {
        PipedOutputStream pos = new PipedOutputStream();
        System.setOut( new PrintStream(pos, true) );

        ConsoleTextArea console = new ConsoleTextArea(displayPane, pos);
        new Thread(console).start();
    }

    public static void redirectErr(JTextArea displayPane)
    {
        PipedOutputStream pos = new PipedOutputStream();
        System.setErr( new PrintStream(pos, true) );

        ConsoleTextArea console = new ConsoleTextArea(displayPane, pos);
        new Thread(console).start();
    }
}