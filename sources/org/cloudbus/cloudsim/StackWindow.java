package org.cloudbus.cloudsim;

import java.awt.*;
   import java.io.*;
   import javax.swing.*;

   public class StackWindow extends JFrame
       implements Thread.UncaughtExceptionHandler {

     private JTextArea textArea;

     public StackWindow(
      String title, final int width, final int height) {
       super(title);
       setSize(width, height);
       textArea = new JTextArea();
       JScrollPane pane = new JScrollPane(textArea);
       textArea.setEditable(false);
       getContentPane().add(pane);
     }

     public void uncaughtException(Thread t, Throwable e) {
       addStackInfo(e);
     }

     public void addStackInfo(final Throwable t) {
       EventQueue.invokeLater(new Runnable() {
         public void run() {
           // Traz a janela para foreground
           setVisible(true);
           toFront();
           // Converte a pilha para string
           StringWriter sw = new StringWriter();
           PrintWriter out = new PrintWriter(sw);
           t.printStackTrace(out);
           // Adiciona a string para o fim da area de texto
           textArea.append(sw.toString());
        }
     });
   }
  }