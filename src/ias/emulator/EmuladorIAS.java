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

/**
 *
 * @author Ian Neumann
 */
public class EmuladorIAS {
    
    private final int [] EMPTY = new int[]{0,0,0,0,0,0,0,0,0,0,
                                     0,0,0,0,0,0,0,0,0,0,
                                     0,0,0,0,0,0,0,0,0,0,
                                     0,0,0,0,0,0,0,0,0,0};
    
    private final int [] LOW_EMPTY = new int[]{0,0,0,0,0,0,0,0,0,0,
                                     0,0,0,0,0,0,0,0,0,0};
    
    private boolean isEmpty(int[] arreglo){
        //verifica si un arreglo contiene puros 0's
        if(Arrays.equals(arreglo, EMPTY)){
            return true;
        }
        else{
            if(Arrays.equals(arreglo, LOW_EMPTY)){
                return true;
            }
            return false;
        }
    }
    
    private boolean side = true; //false para izquierda (1,20), true para derecha

    private String toHex(int[] array){
        //convierte un arreglo de bits a un String de hexadecimales con el tamaño pre-establecido de 10
        String res = Long.toHexString(UnsignedValueOf(array)).toUpperCase();  
        int tam = 10-res.length();
        char[] temporal = new char[10];
        for(int i = 0; i < 10; i++){
            temporal[i] = '0';
        }
        for(int i = 10-1; i >= tam;i--){
            temporal[i] = res.charAt(res.length()-(10-i));
        }
        String output = new String(temporal);
        return output;
    }
    
    private String toHex(int[] array,int size){
        //convierte un arreglo de bits a un String de hexadecimales con el tamaño especifico (size)
        String res = Long.toHexString(UnsignedValueOf(array)).toUpperCase();  
        int tam = size-res.length();
        char[] temporal = new char[size];
        for(int i = 0; i < size; i++){
            temporal[i] = '0';
        }
        for(int i = size-1; i >= tam;i--){
            temporal[i] = res.charAt(res.length()-(size-i));
        }
        String output = new String(temporal);
        return output;
    }
    
    public void dumpMemory(int inicio, int fin){
        if(inicio < 0 || fin >=4096){
            throw new IndexOutOfBoundsException();
        }
        if(inicio > fin){
            throw new NegativeArraySizeException();
        }
        for(int i = inicio; i <= fin; i++){
            Memoria[i] = new int[40];
        }
    }
    
    public void dumpRegisters(boolean AC,boolean MQ, boolean PC, boolean IR, boolean IBR,boolean MAR,boolean MDR){
        if(AC){
            Acumulator = new int[Acumulator.length];
        }
        if(MQ){
            Multiplier = new int[Multiplier.length];
        }
        if(PC){
            ProgramCounter = new int[ProgramCounter.length];
        }
        if(IR){
            InstructionRegister = new int[InstructionRegister.length];
        }
        if(IBR){
            InstructionBufferRegister = new int[InstructionBufferRegister.length];
        }
        if(MAR){
            this.MAR = new int[this.MAR.length];
        }
        if(MDR){
            this.MDR = new int[this.MDR.length];
        }
    }
    
    public String[] getMemoriaRango(int inicio, int fin) throws IndexOutOfBoundsException,NegativeArraySizeException{
        //Regresa un arreglo de strings de valores hexadecimales de cada posicion en memoria desde
        //el inicio hasta el fin, inclusivos
        if(inicio < 0 || fin >=4096){
            throw new IndexOutOfBoundsException();
        }
        if(inicio > fin){
            throw new NegativeArraySizeException();
        }
        String res[] = new String[fin-inicio+1];
        
        for(int i = 0; i <= fin-inicio; i++){
            res[i] = toHex(Memoria[inicio+i]);
        }
        return res;
    }
    
    public String getAcumulator() {
        //regresa un string en hexadecimal del Acumulador
        return toHex(Acumulator,10);
    }

    public String getMultiplier() {
        //regresa un string en hexadecimal del Multiplicador
        return toHex(Multiplier,10);
    }

    public String getProgramCounter() {
        //regresa un string en hexadecimal del PC
        return toHex(ProgramCounter,3);
    }

    public String getInstructionBufferRegister() {
        //regresa un string en hexadecimal del IBR
        return toHex(InstructionBufferRegister,10);
    }

    public String getInstructionRegister() {
        //regresa un string en hexadecimal del IR
        return toHex(InstructionRegister,5);
    }

    public String getMAR() {
        //regresa un string en hexadecimal del MAR
        return toHex(MAR,3);
    }

    public String getMDR() {
        //regresa un string en hexadecimal del MDR
        return toHex(MDR,10);
    }
    private int[] Acumulator;
    private int[] Multiplier;
    private int[] ProgramCounter;
    private int[] InstructionBufferRegister;
    private int[] InstructionRegister;
    private int[] MAR;
    private int[] MDR;
    
    private static int[][] Memoria;

    private LectorDatos lectorDatos;
    private LectorDatos lectorInstrucciones;
    
    public EmuladorIAS(File datos, File instrucciones){
        //Constructor del emulador
        lectorDatos = new LectorDatos(datos);
        lectorInstrucciones = new LectorDatos(instrucciones);
        Acumulator = new int[40];
        Multiplier = new int[40];
        ProgramCounter = new int[12];
        InstructionBufferRegister = new int[40];
        InstructionRegister = new int[20];
        MAR = new int[12];
        MDR = new int[40];
    }
    
    private int [] stringToIntArray(String s){
        //agarra un string en binario y lo convierte a un arreglo de bits
        int[] arreglo = new int[40];
        char[] string = s.toCharArray();
        int cont = 39;
        for(int i = string.length-1; i >= 0;i--){
            arreglo[cont--] = Character.getNumericValue(string[i]);            
        }
        return arreglo;
    }
    
    private int [] stringToIntArray(String s, int size){
        //agarra un string en binario y lo convierte a un arreglo de bits
        int[] arreglo = new int[size];
        char[] string = s.toCharArray();
        int cont = size-1;
        for(int i = string.length-1; i >= string.length-size;i--){
            arreglo[cont--] = Character.getNumericValue(string[i]);            
        }
        return arreglo;
    }
    
    
    
    public void readFiles() throws FileNotFoundException, CompilerException, OverwriteException{
        //lee los datos e instrucciones de los archivos especificados
        Memoria = lectorDatos.leerDatos();
        boolean PC = true;
        int[][] temporal = lectorInstrucciones.leerDatos();
        for(int direccion = 0; direccion < 4096; direccion++){
            if(isEmpty(Memoria[direccion])){
                if(!isEmpty(temporal[direccion])){
                    if(PC){
                        PC = false;
                        ProgramCounter = stringToIntArray(binaryString(direccion),12);
                        long counter = ValueOf(ProgramCounter);
                        InstructionBufferRegister = temporal[direccion].clone();
                        InstructionRegister = getInstruccion(false);
                        ProgramCounter = stringToIntArray(binaryString(counter),12);
                        if(isEmpty(InstructionRegister)){
                            side = false;
                        }
                        else{
                            side = true;
                            ProgramCounter = restar(ProgramCounter, 1);
                        }
                    }
                    Memoria[direccion] = temporal[direccion].clone();
                }
            }
            else{
                if(!isEmpty(temporal[direccion])){
                    throw new OverwriteException();
                }
            }
        }
    }    
    
    private int [] abs(int [] arreglo){
        //Saca el valor absoluto del numero (arreglo)
        long numero = Math.abs(ValueOf(arreglo));
        return stringToIntArray(binaryString(numero),arreglo.length);
    }
      
    private String binaryString(long num){
        //obtiene el String de 1's y 0's del numero (num)
        boolean sign = false;
        int[] string = new int[40];
        if(num < 0){
            sign = true;
        }
        num = Math.abs(num);
        int cont = 39;
        while(num != 0){
            string[cont] = (int)(num%2L);            
            num/=2;
            cont--;
        }
        if(sign){
            return Arrays.toString(A2Complement(string)).replace(" ", "").replace(",", "").replace("[","").replace("]","");
        }
        else {
            return Arrays.toString(string).replace(" ", "").replace(",", "").replace("[","").replace("]","");
     
        }
    }
    
    private int[] sumar(int[] arreglo, long num){
        //Suma los numeros arreglo y num
        long numero;
        numero = ValueOf(arreglo);
        numero += num;
        if(numero >= 0){
            return stringToIntArray(binaryString(numero),arreglo.length);
        }
        else {            
            return A2Complement(stringToIntArray(binaryString(numero),arreglo.length));
        }
    }
    
    private int[] sumar(int[] arreglo, int[] num){
        //Suma los numeros arreglo y num
        long numero;
        numero = ValueOf(arreglo);
        numero += ValueOf(num);
        if(numero >= 0){
            return stringToIntArray(binaryString(numero),arreglo.length);
        }
        else {            
            return A2Complement(stringToIntArray(binaryString(numero),arreglo.length));
        }
    }
    
    private int[] restar(int[] arreglo, long num){        
        // resta del numero (arreglo) el numero (num)
        long numero;
        numero = ValueOf(arreglo);
        numero -= num;
        return stringToIntArray(binaryString(numero),arreglo.length);
       
    }
    
    private int[] restar(int[] arreglo, int[] num){
        // resta del numero (arreglo) el numero (num)
        long numero;
        numero = ValueOf(arreglo);
        numero -= ValueOf(num);
        
        return stringToIntArray(binaryString(numero),arreglo.length);
        
    }
   
    
    private int [] getInstruccion(boolean side){
        //Obtiene una direccion del IBR en la posicion especifica
        int inicio = 0;
        int[] result = new int[20];
        if(side){
            inicio += 20;
        }
        for(int i = 0; i < 20;i++){
            result[i] = InstructionBufferRegister[inicio++];
        }
        return result;
    }
    
    private void getNextInstruccion(){
        //Actualiza el IR con la siguiente instruccion del IBR y si es necesario Actualiza el PC
        if(side){
            side = !side;
            ProgramCounter = sumar(ProgramCounter, 1L);
            InstructionBufferRegister = Memoria[(int)UnsignedValueOf(ProgramCounter)].clone();
            InstructionRegister = getInstruccion(side);
        }
        else{
            side = !side;
            InstructionRegister = getInstruccion(side);
        }
    }
    
    private long UnsignedValueOf(int [] array){
        //obtiene el valor numerico sin signo del arreglo de bits de array
        long suma = 0;
        for(int i = array.length-1; i >= 0;i--){
            suma += array[i]*Math.pow(2, (array.length-1)-i);
        }
        return suma;
        
    }
    
    private long ValueOf(int [] array){
        //obtiene el valor numerico en complemento a2 del arreglo de bits de array
        boolean sign = false;
        if(array[0] == 1){
            array = A2Complement(array);
            sign = true;
        }
        long suma = 0;
        for(int i = array.length-1; i >= 0;i--){
            suma += array[i]*Math.pow(2, (array.length-1)-i);
        }
        if(sign){
            return -suma;
        }    
        else{
            return suma;
        }
    }
    
    private int[] A2Complement(int[] param){
        //saca el complemento a2 del arreglo de bits param
        int[] array = param.clone();
        boolean bool = false;
        for(int i = array.length-1; i >= 0; i--){
            if(bool){
                if(array[i] == 0){
                    array[i] = 1;
                }
                else if(array[i] == 1){
                    array[i] = 0;
                }
            }
            else{
                if(array[i] == 1){
                    bool = true;
                }
            }
        }
        return array;
    }
    
    private int [] Multiplicar(int [] factorA, int [] factorB){
        //multiplica el factorA por el factorB y regresa un arreglo de tamaño 80
        boolean sign = false;
        long a = ValueOf(factorA);
        long b = ValueOf(factorB);
        if(a < 0 && b > 0){
            sign = true;
        }
        if(a > 0 && b < 0){
            sign = true;
        }
        if(sign){
            return A2Complement(stringToIntArray(binaryString(new BigInteger(String.valueOf(a)).multiply(new BigInteger(String.valueOf(b))),80),80));
        }
        else{
            return stringToIntArray(binaryString(new BigInteger(String.valueOf(a)).multiply(new BigInteger(String.valueOf(b))),80),80);
        }
    }
    
    private int [] Multiplicar(int [] factorA, int [] factorB, int size){
        //multiplica el factorA por el factorB y regresa un arreglo del tamaño especificado
        long a = ValueOf(factorA);
        long b = ValueOf(factorB);
        return stringToIntArray(binaryString(new BigInteger(String.valueOf(a)).multiply(new BigInteger(String.valueOf(b))),size),size);
    }
    
    private String binaryString(BigInteger num, int size){
        //obtiene el arreglo de bits del numero (num), con un tamaño en especifico (size)
        boolean sign = false;
        int[] string = new int[size];
        
        if(num.compareTo(new BigInteger("0")) == -1){
            sign = true;
        }
        num = num.abs();
        int cont = size-1;
        while(num.compareTo(new BigInteger("0")) != 0){
            string[cont] = (num.mod(new BigInteger("2"))).intValue();            
            num = num.divide(new BigInteger("2"));
            cont--;
        }
        if(sign){
            return Arrays.toString(A2Complement(string)).replace(" ", "").replace(",", "").replace("[","").replace("]","");
        }
        else {
            return Arrays.toString(string).replace(" ", "").replace(",", "").replace("[","").replace("]","");
     
        }
    }
    
    private int[] shiftRight(int [] param){
        //recorre un bit hacia la derecha el arreglo param
        int[] array = param.clone();
        int temp = 0;
        for(int i = param.length-1; i > 0; i--){
            array[i] = array[i-1];
        }
        array[0] = 0;
        return array;
    }
    
    private int[] shiftLeft(int [] param){
        //recorre un bit hacia la izquierda el arreglo param
        int[] array = param.clone();
        int temp = 0;
        for(int i = 0; i < param.length-1; i++){
            array[i] = array[i+1];
        }
        array[param.length-1] = 0;
        return array;
    }
    
    private void Mul(int[] factorA, int[] factorB){
        //Multiplica el factorA por el factorB
        //dejando en el Multiplicador los 40 bits menos significativos
        //y en el Acumulador los 40 bits mas significativos
        int[] lessSignificant = new int[40];
        int[] moreSignificant = new int[40];
        int[] res = Multiplicar(factorA, factorB,80);
        for(int i = 0; i < 80; i++){
            if(i < 40){
                moreSignificant[i] = res[i];
            }
            else {
                lessSignificant[i-40] = res[i];
            }
        }    
        Multiplier = lessSignificant.clone();
        Acumulator = moreSignificant.clone();
    }
    
    private boolean isPositive(int[] array){
        //checa si el numero es positivo en complemento a2
        if(array[0] == 0){
            return true;
        }
        else{
            return false;
        }
    }
    
    private int [] Dividir(int [] num, int [] den){
        //divide el num (numerador) entre el den (denominador)
        //dejando en los bits mas significativos el cociente
        //y en los menos significantivos el residuo
        long numerador = ValueOf(num);
        long denominador = ValueOf(den);
        long resultado = numerador/denominador;
        long res = numerador%denominador;
        int[] cociente = stringToIntArray(binaryString(resultado));
        int[] residuo = stringToIntArray(binaryString(res));
        int[] toReturn = new int[80];
        for(int i = 0; i<80;i++){
            if(i <40){
                toReturn[i] = cociente[i];
            }
            else{
                toReturn[i] = residuo[i-40];
            }
        }
        return toReturn;
    }
    
    
    
    private void Div(int [] num, int [] den){
        //divide el num (numerador) entre el den (denominador)
        //dejando el resultado en el multiplicador y
        //el residuo en el Acumulador
        long numerador = ValueOf(num);
        long denominador = ValueOf(den);
        long resultado = numerador/denominador;
        long res = numerador%denominador;
        Multiplier = stringToIntArray(binaryString(resultado));
        Acumulator = stringToIntArray(binaryString(res));
    }
    
    private void ejecutar(int[] instruction) throws CompilerException{
        int address;
        switch ((int)ValueOf(instruction)){
            case(1):
                //LOAD M(X) -> Transfer M(X) to the accumulator
                MDR = Memoria[(int)UnsignedValueOf(MAR)].clone();
                Acumulator = MDR.clone();
                break;
            case(2):
                //LOAD -M(X) -> Transfer -M(X) to the accumulator
                MDR = Memoria[(int)UnsignedValueOf(MAR)].clone();
                Acumulator = A2Complement(MDR);
                break;
            case(3):
                //LOAD |M(X)| -> Transfer absolute value of M(X) to the accumulator
                MDR = Memoria[(int)UnsignedValueOf(MAR)].clone();
                Acumulator = abs(MDR);
                
                break;
            case(4):
                //LOAD -|M(X)| -> Transfer minus absolute value of M(X) to the accumulator
                MDR = Memoria[(int)UnsignedValueOf(MAR)].clone();
                Acumulator = A2Complement(abs(MDR));                
                break;
            case(5):
                //ADD M(X) -> Add M(X) to AC; put the result in AC
                MDR = Memoria[(int)UnsignedValueOf(MAR)].clone();
                Acumulator = sumar(MDR, Acumulator);
                break;
            case(6):
                //SUB M(X) -> Subtract M(X) from AC; put the result in AC
                MDR = Memoria[(int)UnsignedValueOf(MAR)].clone();
                Acumulator = restar(Acumulator,MDR);
                break;
            case(7):
                //ADD |M(X)| -> Add |M(X)| to AC; put the result in AC
                MDR = Memoria[(int)UnsignedValueOf(MAR)].clone();
                Acumulator = sumar(abs(MDR), Acumulator);
                break;
            case(8):
                //SUB |M(X)| -> Subtract |M(X)| from AC; put the remainder in AC
                MDR = Memoria[(int)UnsignedValueOf(MAR)].clone();
                Acumulator = restar(Acumulator, abs(MDR));
                break;
            case(9):
                //LOAD MQ, M(X) -> Transfer contents of memory location X to MQ
                MDR = Memoria[(int)UnsignedValueOf(MAR)].clone();
                Multiplier = MDR.clone();
                break;
            case(10):
                //LOAD MQ -> Transfer contents of register MQ to the accumulator AC 
                Acumulator = Multiplier.clone();
                break;
            case(11):
                //MUL M(X) -> Multiply M(X) by MQ; put most significant bits of result in AC,
                //put least significant bits in MQ
                MDR = Memoria[(int)UnsignedValueOf(MAR)].clone();
                Mul(MDR, Multiplier);
                break;
            case(12):
                //DIV M(X) -> Divide AC by M(X); put the quotient in MQ and the remainder in AC
                MDR = Memoria[(int)UnsignedValueOf(MAR)].clone();
                Div(Acumulator,MDR);
                break;
            case(13):
                //JUMP M(X) -> Take next instruction from left half of M(X)
                side = true;
                MDR = Memoria[(int)UnsignedValueOf(MAR)].clone();
                ProgramCounter = restar(MAR,1);
                break;
            case(14):
                //JUMP M(X) -> Take next instruction from right half of M(X)
                side = false;
                MDR = Memoria[(int)UnsignedValueOf(MAR)].clone();
                ProgramCounter = MAR.clone();
                break;
            case(15):
                //JUMP+M(X,0:19) -> If number in the accumulator is nonnegative, 
                //take next instruction from left half of M(X)
                if(ValueOf(Acumulator) >= 0){
                    side = true;
                    MDR = Memoria[(int)UnsignedValueOf(MAR)].clone();
                    ProgramCounter = restar(MAR,1);
                }
                break;
            case(16):
                //JUMP+M(X,20:39) -> If number in the accumulator is nonnegative, 
                //take next instruction from right half of M(X)
                if(ValueOf(Acumulator) >= 0){
                    side = true;
                    MDR = Memoria[(int)UnsignedValueOf(MAR)].clone();
                    ProgramCounter = restar(MAR,1);
                }
                break;
            case(33):
                // STOR M(X) -> Transfer contents of accumulator to memory location X
                Memoria[(int)UnsignedValueOf(MAR)] = Acumulator.clone();
                break;
            case(18):
                // STOR M(X,8:19) -> Replace left address field at M(X) by 12 rightmost bits of AC
                address = (int)UnsignedValueOf(MAR);
                for(int i = 8; i < 20; i++){
                    Memoria[address][i] = Acumulator[20+i];                                
                }
                break;
            case(19):
                // STOR M(X,28:39) -> Replace right address field at M(X) by 12 rightmost bits of AC
                address = (int)UnsignedValueOf(MAR);
                for(int i = 28; i < 40; i++){
                    Memoria[address][i] = Acumulator[20+i];
                }
                break;
            case(20):
                //LSH (Shift Left) -> Multiply accumulator by 2; i.e., shift left one bit position
                Acumulator = shiftLeft(Acumulator);
                break;
            case(21):
                //RSH -> SHIFT RIGHT -> Divide accumulator by 2; i.e., shift right one bit position
                Acumulator = shiftRight(Acumulator);
                break;
            default:
                //INVALID INSTRUCTION
                throw new CompilerException();
                
        }
    }
        
    public void run() throws CompilerException{
        //Es un ciclo infinito hasta que el programa encuentra un HALT, 
        //cada ciclo agarra la siguiente instruccion "Instruction Register" 
        //y luego ejecuta dicha instruccion "Instruction Register".

        int[] instruction = new int[8];
        while(true){
            getNextInstruccion();
            if(isEmpty(InstructionRegister)){
                break;
            }
            for(int i = 0; i < 8; i++){
                instruction[i] = InstructionRegister[i];
            }
            for(int i = 8; i < 20;i++){
                MAR[i-8] = InstructionRegister[i];
            }
            ejecutar(instruction);
        }
    }
}
