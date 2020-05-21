package usbdrivedectector.process;


import java.io.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CommandExecutor implements Closeable {
    

    private final BufferedReader input;
    private final Process process;

    public CommandExecutor(final String command) throws IOException {        
        
        process = Runtime.getRuntime().exec(command);

        input = new BufferedReader(new InputStreamReader(process.getInputStream()));
    }

    public void processOutput(final Consumer<String> method) throws IOException{
        String outputLine;
        while ((outputLine = this.readOutputLine()) != null) {
            method.accept(outputLine);
        }
    }

    public boolean checkOutput(final Predicate<String> method) throws IOException{
        String outputLine;
        while ((outputLine = this.readOutputLine()) != null) {
            if (method.test(outputLine)){
                return true;
            }
        }

        return false;
    }

    private String readOutputLine() throws IOException {
        if(input == null) {
            throw new IllegalStateException("You need to call 'executeCommand' method first");
        }
        
         String outputLine = input.readLine();
         
         if(outputLine != null) {
             return outputLine.trim();
         }
         
         return null;
    }

    @Override
    public void close() throws IOException {
        if (input != null) {
            try {
                input.close();
            } catch (IOException e) {                
            }
        }

        if (process != null) {
            process.destroy();
        }
    }

}
