import java.io.BufferedReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Uso: java -cp bin Main tests\\validas.txt");
            return;
        }

        Path arquivo = Paths.get(args[0]);
        int total = 0;
        int aceitas = 0;

        BufferedReader leitor = Files.newBufferedReader(arquivo, StandardCharsets.UTF_8);
        try {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                if (!linha.isEmpty() && linha.charAt(0) == '\uFEFF') {
                    linha = linha.substring(1);
                }
                String texto = linha.trim();
                if (texto.isEmpty() || texto.startsWith("#")) {
                    continue;
                }

                total++;
                System.out.println("---- " + texto + " ----");
                if (reconhecer(texto)) {
                    aceitas++;
                    System.out.println("Resultado: ACEITA");
                } else {
                    System.out.println("Resultado: REJEITADA");
                }
                System.out.println();
            }
        } finally {
            leitor.close();
        }

        System.out.println("Arquivo: " + arquivo.getFileName());
        System.out.println(aceitas + " aceitas, " + (total - aceitas) + " rejeitadas, " + total + " no total.");
    }

    private static boolean reconhecer(String cadeia) throws Exception {
        AutomatoPilha pda = new AutomatoPilha();
        Lexer lexer = new Lexer(new StringReader(cadeia));
        System.out.println(AutomatoPilha.cabecalho());

        while (pda.getEstado() == AutomatoPilha.Estado.q0) {
            Token token = lexer.yylex();
            if (token == null) {
                pda.encerrar();
                break;
            }
            pda.consumir(token);
        }

        for (int i = 0; i < pda.getPassos().size(); i++) {
            System.out.println(pda.getPassos().get(i));
        }
        if (!pda.aceitou()) {
            System.out.println("Motivo: " + pda.getMotivo());
        }
        return pda.aceitou();
    }
}
