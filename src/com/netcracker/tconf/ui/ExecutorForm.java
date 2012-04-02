package com.netcracker.tconf.ui;

import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.netcracker.tconf.annotated.Executor;
import com.netcracker.tconf.model.Test;
import com.netcracker.util.Label;
import com.netcracker.util.Label.Bundle;

public class ExecutorForm
{
    private final Bundle L = Label.getBundle("ui");
    
    private final Executor executor;

    private JDialog dialog;

    protected JTextArea output;

    private boolean isExecuting;

    private Thread thread;

    private PrintStream out = new PrintStream(new OutputStream() 
    {
        @Override
        public void write(byte[] b, int off, int len) throws IOException
        {
            output.append(new String(b, off, len));
            output.setCaretPosition(output.getDocument().getLength());
        }

        @Override
        public void write(int b) throws IOException
        {
            output.append("" + ((char) b));
            output.setCaretPosition(output.getDocument().getLength());
        }
    }, 
    true);

    protected PrintStream realOut;
    
    @SuppressWarnings("serial")
    public ExecutorForm(JFrame parent, Executor executor)
    {
        this.executor = executor;
        
        this.dialog = new JDialog(parent, true)
        {{
            JScrollPane scroll = new JScrollPane(output = new JTextArea());
            add(scroll);
            output.setEditable(false);
            output.setFont(new Font("Monospaced", Font.PLAIN, output.getFont().getSize()));
            output.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            setSize(640, 480);
            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    close(); }});
        }};
    }
    
    public void execute(final Test test)
    {
        isExecuting = true;
        dialog.setTitle(L.get("ui.executor.title", test.getTitle()));
        output.setText("");
        thread = new Thread() {
            public void run() 
            {
                realOut = System.out;
                System.setOut(out);
                executor.execute(test);
                System.setOut(realOut);
                isExecuting = false;
                dialog.setTitle(L.get("ui.executor.title.stopped", test.getTitle()));
            }
        };
        thread.start();
        dialog.setVisible(true);
    }
    
    private void close()
    {
        if (isExecuting &&
                JOptionPane.YES_OPTION ==
                JOptionPane.showConfirmDialog(dialog, 
                        L.get("ui.executor.stop_execution"), 
                        L.get("ui.executor.stop.title"), JOptionPane.YES_NO_OPTION)) {
            
            thread.interrupt();
            try {
                thread.join(5000);
            }
            catch (InterruptedException e) { 
                System.setOut(realOut);
                isExecuting = false;
            }
        }
        if (! isExecuting)
            dialog.setVisible(false);
    }
}




