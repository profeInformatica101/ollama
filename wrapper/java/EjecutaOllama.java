package com;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class EjecutaOllama {
	
	public static void main(String[] args) {
        try {
        	ProcessBuilder pb = new ProcessBuilder(
        		    "bash", "-c",
        		    "echo 'Explica la máquina virtual de Java' | ollama run deepseek-r1:32b --format json | jq -r '.response'"
        		);
            
            // 🔹 Mezclar salida y error
            pb.redirectErrorStream(true);

            Process proceso = pb.start();

            // 🔹 Leer salida del proceso y enviarla a la consola de Eclipse
            try (BufferedReader br = new BufferedReader(new InputStreamReader(proceso.getInputStream()))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    // Elimina secuencias ANSI y otros caracteres de control
                    String limpio = linea.replaceAll("\\u001B\\[[;?0-9]*[a-zA-Z]", "")
                                         .replaceAll("\\p{Cntrl}", ""); // quita controles no imprimibles

                    if (!limpio.isBlank()) { // solo imprime si queda texto útil
                        System.out.println("[OLLAMA] " + limpio);
                    }
                }
            }

            int exit = proceso.waitFor();
            System.out.println("Proceso terminado con código: " + exit);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
