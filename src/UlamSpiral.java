import java.awt.*;
import java.io.*;

public
    class UlamSpiral {
    public static void main(String[] args) throws FileNotFoundException {
        new FileOutputStream("prime_numbers.bin");
        int width = 500;
        int height = 500;
        int[] calculatedPrimes = calculatePrimes(width, height);
        new Spiral(width, height, calculatedPrimes);
    }

    public static boolean checkPrime(int numToCheck) {
        boolean isPrime = true;
        for (int i = 2; i <= Math.sqrt(numToCheck); i++) {
            if (numToCheck % i == 0) {
                isPrime = false;
                break;
            }
        }
        return isPrime;
    }

    public static int[] calculatePrimes(int width, int height) {
        int[] primes = new int[]{};
        int numToCheck = 2;
        int bytesRequired = 1;
        int powerOfTwo = 8;
        long headerBytes = 0;
        while(numToCheck <= width * height * 10) {
            System.out.println(numToCheck);
            if (checkPrime(numToCheck)) {
                if (numToCheck >= Math.pow(2, powerOfTwo) - 1){
                    outputLine(headerBytes, bytesRequired, primes);
                    bytesRequired++ ;
                    powerOfTwo += 8;
                    headerBytes = 0;
                }
                headerBytes++;
                int[] temp = new int[primes.length + 1];
                System.arraycopy(primes, 0, temp, 0, primes.length);
                temp[temp.length - 1] = numToCheck;
                primes = temp;
            }
            numToCheck++;
        }
        if (headerBytes != 0) outputLine(headerBytes, bytesRequired, primes);
        return primes;
    }

    public static void outputLine(long headerBytes, int bytesRequired, int[] primes) {
        int numbersInLine = (int) headerBytes;
        System.out.println(headerBytes);
        int counter;
        int headerByteShifts = 1;
        while(headerBytes - (Math.pow(2, headerByteShifts * 8) - 1) > 0) {
            headerByteShifts++;
        }
        byte[] byteBuffer = new byte[bytesRequired * (int)headerBytes + 8];
        try {
            try (FileOutputStream fileOS = new FileOutputStream("prime_numbers.bin", true)) {
                for (int i = 0, j = 0; 8 - headerByteShifts + i <= 7; i++, j++) {
                    byteBuffer[8 - headerByteShifts + i] = (byte)((((headerBytes >> ((headerByteShifts - (j + 1)) * 8)) << 56) >> 56));
                }
                counter = 8;
                for (int i = 0; primes.length - numbersInLine + i < primes.length; i++) {
                    for (int j = 0; j < bytesRequired; j++, counter++) {
                        byteBuffer[counter] = (byte)((((primes[primes.length - numbersInLine + i] >> ((bytesRequired - (j + 1)) * 8)) << 24) >> 24));
                    }
                }
                fileOS.write(byteBuffer);
                fileOS.write('\n');
                fileOS.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

class Spiral extends Frame {
    public int[] storedValues;
    public Spiral(int width, int height, int[] calculatedPrimes) {
        super();
        super.setSize(width, height);
        this.storedValues = calculatedPrimes;
        super.setVisible(true);
        Color myDarkGray = new Color(22, 22, 22);
        super.setBackground(myDarkGray);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Color myGreen = new Color(70, 185, 50);
        g.setColor(myGreen);
        for (int i = 0, j = 0, counter = 0, index = 0; counter < storedValues[storedValues.length - 1];) {
            for (;i <= j && index < storedValues.length; ++i, counter++) {
                if (counter == storedValues[index]) {
                    g.drawRect(this.getWidth() / 2 + i, this.getHeight() /2 + j, 0, 0);
                    index++;
                }
            }
            for(;j != -i && index < storedValues.length; --j, counter++) {
                if (counter == storedValues[index]) {
                    g.drawRect(this.getWidth() / 2 + i, this.getHeight() /2 + j, 0, 0);
                    index++;
                }
            }
            for(;i != j && index < storedValues.length; --i, counter++) {
                if (counter == storedValues[index]) {
                    g.drawRect(this.getWidth() / 2 + i, this.getHeight() /2 + j, 0, 0);
                    index++;
                }
            }
            for(;j != -i && index < storedValues.length; ++j, counter++) {
                if (counter == storedValues[index]) {
                    g.drawRect(this.getWidth() / 2 + i, this.getHeight() /2 + j, 0, 0);
                    index++;
                }
            }
        }
    }
}