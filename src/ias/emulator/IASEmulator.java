/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ias.emulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ian Neumann
 */
public class IASEmulator {
    /*private static int[][] Memoria = new int[4096][40];
    private static final int [] empty = new int[] {0,0,0,0,0,0,0,0,0,0,
                                    0,0,0,0,0,0,0,0,0,0,
                                    0,0,0,0,0,0,0,0,0,0,
                                    0,0,0,0,0,0,0,0,0,0,};
    
    private static boolean isEmpty(int[] word){
        if(Arrays.equals(word,empty)){
            return true;
        }
        return false;
    }*/
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //Usado para debuggear, porfavor omitir
        
        File datos = new File("datos.txt");
        File instrucciones = new File("instrucciones.txt");
        EmuladorIAS emulador =new EmuladorIAS(datos, instrucciones);
        try {
           emulador.readFiles();
           emulador.run();
        } catch (CompilerException e) {
            System.out.println("Error de compilacion");
        }
        catch (FileNotFoundException e){
            System.out.println("No se encontro el archivo");
        }
        catch(OverwriteException e){
            System.out.println("Al momento de leer los archivos se intento sobreescribir un lugar de memoria");
        }
        /*Scanner scanner = new Scanner(System.in);
        LectorDatos lectorDatos = new LectorDatos(new File("datos.txt"));
        LectorDatos lectorInstrucciones = new LectorDatos(new File("instrucciones.txt"));
        try {
            Memoria = lectorDatos.leerDatos();
            int[][] temporal = lectorInstrucciones.leerDatos();
            int cont = 0;
            for(int[] i :temporal){
                
                if(!isEmpty(i)){
                    if(isEmpty(Memoria[cont])){
                        Memoria[cont] = i.clone();
                    }
                    else{
                        System.out.println("Error! se intento utilizar un lugar de memoria ya utilizado previamente como dato");
                        System.out.println("¿Desea sobreescribir el dato? (Y/N), ó (R) para reiniciar");
                        String cual  = scanner.nextLine();
                        while(true){
                            if(cual.toUpperCase().trim().equals("Y")){
                                Memoria[cont] = i.clone();
                            }
                            else if(cual.toUpperCase().trim().equals("N")){
                                break;
                            }
                            else if(cual.toUpperCase().trim().equals("R")){
                                throw new CompilerException();
                            } 
                            else{
                                System.out.println("Opcion invalida\n¿Desea sobreescribir el dato? (Y/N), ó (R) para reiniciar");
                            }
                        }
                    }
                }
                cont++;
            }
        } catch (FileNotFoundException ex) {
            System.out.println("No se encontro un archivo");
        } catch (CompilerException ex) {
            Logger.getLogger(IASEmulator.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        
        
    }
    
}
