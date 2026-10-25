import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

/**
 * Automato para parenteses balanceados.
 * delta(q0, '(', Z0) = (q0, X Z0)
 * delta(q0, '(', X)  = (q0, X X)
 * delta(q0, ')', X)  = (q0, epsilon)
 * No fim da entrada, Z0 leva a q_aceita.
 */
public class AutomatoPilha {

    public enum Estado {
        q0,
        q_aceita,
        q_rejeita
    }

    private Estado estado = Estado.q0;
    private final Deque<String> pilha = new ArrayDeque<String>();
    private final List<String> passos = new ArrayList<String>();
    private String motivo = "";

    public AutomatoPilha() {
        pilha.push("Z0");
    }

    public static String cabecalho() {
        return String.format("%-6s %-8s %-6s %-20s %-12s %s",
                "Passo", "Simbolo", "Topo", "Operacao", "Estado", "Pilha");
    }

    public void consumir(Token token) {
        if (estado != Estado.q0) {
            return;
        }

        String topo = pilha.peek();
        String simbolo = token.getLexema();

        if (token.getTipo() == Token.Tipo.ABRE) {
            pilha.push("X");
            anotar(simbolo, topo, "EMPILHA X");
            return;
        }

        if (token.getTipo() == Token.Tipo.FECHA) {
            if ("X".equals(topo)) {
                pilha.pop();
                anotar(simbolo, topo, "DESEMPILHA X");
            } else {
                estado = Estado.q_rejeita;
                motivo = "')' na coluna " + token.getColuna() + " com a pilha em Z0";
                anotar(simbolo, topo, "sem transicao");
            }
            return;
        }

        estado = Estado.q_rejeita;
        motivo = "'" + token.getLexema() + "' na coluna " + token.getColuna() + " esta fora do alfabeto";
        anotar(simbolo, topo, "rejeita simbolo");
    }

    public void encerrar() {
        if (estado != Estado.q0) {
            return;
        }
        String topo = pilha.peek();
        if ("Z0".equals(topo)) {
            estado = Estado.q_aceita;
            anotar("fim", topo, "aceita");
        } else {
            estado = Estado.q_rejeita;
            motivo = "faltou fechar " + (pilha.size() - 1) + " parentese(s)";
            anotar("fim", topo, "sobrou abertura");
        }
    }

    public Estado getEstado() {
        return estado;
    }

    public boolean aceitou() {
        return estado == Estado.q_aceita;
    }

    public String getMotivo() {
        return motivo;
    }

    public List<String> getPassos() {
        return passos;
    }

    private void anotar(String simbolo, String topo, String operacao) {
        passos.add(String.format("%-6d %-8s %-6s %-20s %-12s %s",
                passos.size() + 1, simbolo, topo, operacao, estado, pilhaTexto()));
    }

    private String pilhaTexto() {
        StringBuilder sb = new StringBuilder("[");
        Iterator<String> it = pilha.descendingIterator();
        while (it.hasNext()) {
            sb.append(it.next());
            if (it.hasNext()) {
                sb.append(' ');
            }
        }
        sb.append(']');
        return sb.toString();
    }
}
