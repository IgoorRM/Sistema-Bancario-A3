import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class ContaBancaria {
    private String login;
    private String senha;
    private double saldo;

    public ContaBancaria(String login, String senha, double saldo) {
        this.login = login;
        this.senha = senha;
        this.saldo = saldo;
    }

    public boolean autenticar(String senha) {
        return this.senha.equals(senha);
    }

    public void depositar(double valor) {
        saldo += valor;
    }

    public boolean sacar(double valor) {
        if (valor <= saldo) {
            saldo -= valor;
            System.out.println("Saque de " + valor + " realizado com sucesso.");
            return true;
        } else {
            System.out.println("Saldo insuficiente para saque.");
            return false;
        }
    }

    public double getSaldo() {
        return saldo;
    }

    public String getLogin() {
        return login;
    }
}

class SistemaBancario {
    private Map<String, ContaBancaria> contas;

    public SistemaBancario() {
        this.contas = new HashMap<>();
    }

    public void abrirConta(String login, String senha, double saldoInicial) {
        ContaBancaria novaConta = new ContaBancaria(login, senha, saldoInicial);
        contas.put(login, novaConta);
        System.out.println("Conta aberta com sucesso para o usuário " + login);
    }

    public ContaBancaria buscarConta(String login) {
        return contas.get(login);
    }

    public boolean transferir(ContaBancaria origem, ContaBancaria destino, double valor) {
        if (origem.getSaldo() >= valor) {
            origem.sacar(valor);
            destino.depositar(valor);
            System.out.println("Transferência de " + valor + " realizada com sucesso de " + origem.getLogin() + " para " + destino.getLogin() + ".");
            return true;
        } else {
            System.out.println("Saldo insuficiente para transferência.");
            return false;
        }
    }
}

public class Main {
    public static void main(String[] args) {
        SistemaBancario sistema = new SistemaBancario();
        Scanner scanner = new Scanner(System.in);
        boolean executando = true;

        while (executando) {
            System.out.println("Escolha uma opção:");
            System.out.println("1. Abrir uma nova conta bancária");
            System.out.println("2. Autenticar um cliente");
            System.out.println("3. Sair do sistema");
            int opcaoPrincipal = scanner.nextInt();

            if (opcaoPrincipal == 1) {
                System.out.println("Abertura de conta bancária");
                System.out.print("Digite o login: ");
                String novoLogin = scanner.next();
                System.out.print("Digite a senha: ");
                String novaSenha = scanner.next();
                System.out.print("Digite o saldo inicial: ");
                double saldoInicial = scanner.nextDouble();
                sistema.abrirConta(novoLogin, novaSenha, saldoInicial);
            } else if (opcaoPrincipal == 2) {
                System.out.println("\nAutenticação do cliente");
                System.out.print("Digite o login: ");
                String loginAutenticacao = scanner.next();
                System.out.print("Digite a senha: ");
                String senhaAutenticacao = scanner.next();
                ContaBancaria conta = sistema.buscarConta(loginAutenticacao);

                if (conta != null && conta.autenticar(senhaAutenticacao)) {
                    System.out.println("Cliente autenticado com sucesso.");
                    int opcaoCliente = 0;
                    while (opcaoCliente != 3) {
                        System.out.println("\nEscolha a operação desejada:");
                        System.out.println("1. Transferência de dinheiro");
                        System.out.println("2. Sacar dinheiro");
                        System.out.println("3. Sair");
                        opcaoCliente = scanner.nextInt();
                        if (opcaoCliente == 1) {
                            System.out.print("Digite o login da conta de destino: ");
                            String loginDestino = scanner.next();
                            ContaBancaria contaDestino = sistema.buscarConta(loginDestino);
                            if (contaDestino != null) {
                                System.out.print("Digite o valor a ser transferido: ");
                                double valorTransferencia = scanner.nextDouble();
                                if (sistema.transferir(conta, contaDestino, valorTransferencia)) {
                                    System.out.println("Saldo na conta de origem: " + conta.getSaldo());
                                    System.out.println("Saldo na conta de destino: " + contaDestino.getSaldo());
                                }
                            }
                        } else if (opcaoCliente == 2) {
                            System.out.print("Digite o valor a ser sacado: ");
                            double valorSaque = scanner.nextDouble();
                            conta.sacar(valorSaque);
                            System.out.println("Saldo atual: " + conta.getSaldo());
                        } else if (opcaoCliente == 3) {
                            System.out.println("Saindo do sistema. Obrigado!");
                            executando = false;
                            break;
                        } else {
                            System.out.println("Opção inválida. Tente novamente.");
                        }
                    }
                } else {
                    System.out.println("Falha na autenticação do cliente. Verifique o login e a senha.");
                }
            } else if (opcaoPrincipal == 3) {
                System.out.println("Saindo do sistema. Obrigado!");
                executando = false;
            } else {
                System.out.println("Opção inválida. Tente novamente.");
            }
        }

        scanner.close();
    }
}