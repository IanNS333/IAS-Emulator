/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ias.emulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author Ian Neumann
 */
public class LectorDatos {
    
    private File datos;
    private int[][] memoria;
    //private int[][] memoria = new int[4096][40];
    
    
    public LectorDatos(File datos){
        //crea un objeto y establece el archivo de donde va a leer
        this.datos = datos;
        memoria = new int[4096][40];
    }
    
    
    
    private long obtenerValor(String s) throws CompilerException{
        // agarra un string en hexadecimal y regresa su valor en decimal
        // omitiendo characteres que no sean ni numeros ni letras
        // llamando a la funcion obtenerValor <char> (vease la documentacion del metodo)
        
        char[] string = s.toCharArray();
        char[] numeros = new char[10];
        int cont = 0;
        long suma = 0;
        
        for(int i = 0; i < 10; i++){
            if(!Character.isLetterOrDigit(string[i])){
                throw new CompilerException();
            }
            numeros[i] = string[i];
            cont = i+1;
        }
        /*for(char c : string){
            if(Character.isLetterOrDigit(c)){
                numeros[cont++] = c;
            }
        }*/
        
        if(cont < 10){
            throw new CompilerException();
        }
        
        for(int i = 9; i >= 0; i--){
            suma += obtenerValor(numeros[i])* Math.pow(16, 9-i);
        }
        return suma;
    }
            
    private int obtenerValor(char c) throws CompilerException{
        // agarra un caracter y regresa su valor en hexadecimal
        // si el caracter no es hexadecimal se manda un CompilerException
        
        switch(c){
            case '0':
                return 0;
            case '1':
                return 1;
            case '2':
                return 2;
            case '3':
                return 3;
            case '4':
                return 4;
            case '5':
                return 5;
            case '6':
                return 6;
            case '7':
                return 7;
            case '8':
                return 8;
            case '9':
                return 9;
            case 'A':
                return 10;
            case 'B':
                return 11;
            case 'C':
                return 12;
            case 'D':
                return 13;
            case 'E':
                return 14;
            case 'F':
                return 15;
            default:
                throw new CompilerException();
        }
            
    }
    
    private int obtenerDireccion(String s) throws CompilerException{
        // agarra un string y obtiene los primeros tres hexadecimales
        // y los manda a obtenerValor <char> (vease la documentacion del metodo)
        // que es la direccion a la que hace referencia
        // por ultimo regresa esa direccion
        
        char[] string = s.toCharArray();
        char[] numeros = new char[3];
        int cont = 0;
        long suma = 0;
        
        for(char c : string){
            if(cont == 3){
                break;
            }
            if(Character.isLetterOrDigit(c)){
                numeros[cont++] = c;
            }
        }
        int high = obtenerValor(numeros[0]);
        int mid = obtenerValor(numeros[1]);
        int low = obtenerValor(numeros[2]);
        return (high*16*16)+(mid*16)+low;
    }
    
    private int [] stringToIntArray(String s){
        //agarra un string en binario y lo convierte a un arreglo de enteros
        int[] arreglo = new int[40];
        char[] string = s.toCharArray();
        int cont = 39;
        for(int i = string.length-1; i >= 0;i--){
            arreglo[cont--] = Character.getNumericValue(string[i]);            
        }
        return arreglo;
    }
    
    public int[][] leerDatos() throws FileNotFoundException, CompilerException{
    /*  lee el archivo especificado en el constructor
        luego va linea por linea quitando todos los caracteres blancos, y las pone mayusculas
        cada linea la manda al metodo obtenerDireccion (vease la documentacion del metodo)
        despues de que tenemos la direccion removemos los datos de la direccion
        esa linea la mandamos al metodo obtenerValor <String> (vease la documentacion del metodo)
        en la memoria se establece el la representacion en bits con el metodo toBinaryString y luego
        ese string se manda al metodo stringToIntArray(vease la documentacion del metodo)
        y por ultimo se regresa la memoria a quien invoco el metodo                                   */
        
        int direccion;
        long valor;
        Scanner scanner = new Scanner(datos);
        String linea;
        int cont = 0;
        while(scanner.hasNextLine()){
            linea = scanner.nextLine();
            if(linea.equals("")){
                continue;
            }
            linea = linea.replaceAll("\\s", ""); 
            linea = linea.toUpperCase();
            if(linea.startsWith("#")){
                continue;
            }
            direccion = obtenerDireccion(linea);
            linea = linea.substring(3);
            if(linea.startsWith(":")){
                linea = linea.substring(1);
            }
            valor = obtenerValor(linea);
            memoria[direccion] = stringToIntArray(Long.toBinaryString(valor));
            
        }
        return memoria;      
    }
}
