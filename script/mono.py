from locust import HttpUser, task, between

class MyApiUser(HttpUser):
    wait_time = between(1, 3)

    def on_start(self):
        self.conta_id = self.criar_conta()
        self.produto_ids = self.criar_produtos()

    def criar_conta(self):
        conta_body = {
            "email": "maykeesa.contato@gmail.com",
            "nome": "Mayke Erick Santos Andrade",
            "cpf": "12345678900",
            "cartoes": [
                {
                    "nome": "Mayke Erick Santos Andrade",
                    "numero": "4111111111111111",
                    "dataValidade": "12/2026",
                    "cvv": "123",
                    "padrao": True
                },
                {
                    "nome": "Mayke Erick Santos Andrade",
                    "numero": "5500000000000004",
                    "dataValidade": "11/2025",
                    "cvv": "456",
                    "padrao": False
                }
            ],
            "enderecos": [
                {
                    "logradouro": "Rua Exemplo",
                    "numero": 123,
                    "complemento": "Apartamento 101",
                    "bairro": "Centro",
                    "cidade": "Aracaju",
                    "estado": "SE",
                    "cep": "01000-000",
                    "pais": "Brasil",
                    "padrao": True
                },
                {
                    "logradouro": "Rua AAAAA",
                    "numero": 6564,
                    "complemento": "Apartamento 500",
                    "bairro": "São José",
                    "cidade": "Aracaju",
                    "estado": "SE",
                    "cep": "01000-000",
                    "pais": "Brasil",
                    "padrao": False
                }
            ]
        }
        with self.client.post("/conta", json=conta_body, name="Criar Conta", catch_response=True) as response:
            if response.status_code == 201:
                return response.json().get("id")
            else:
                response.failure("Falha ao criar conta")
                return None

    def criar_produtos(self):
        produtos = [
            {
                "nome": "Caderno",
                "categoria": "ESCOLAR",
                "descricao": "Tilibra",
                "preco": 20,
                "disponibilidade": "DISPONIVEL",
                "estoque": 100
            },
            {
                "nome": "Caneta",
                "categoria": "ESCOLAR",
                "descricao": "Faber-Castell",
                "preco": 5,
                "disponibilidade": "DISPONIVEL",
                "estoque": 300
            }
        ]
        ids = []
        for produto in produtos:
            with self.client.post("/produto", json=produto, name="Criar Produto", catch_response=True) as response:
                if response.status_code == 201:
                    ids.append(response.json().get("id"))
                else:
                    response.failure("Falha ao criar produto")
        return ids

    @task
    def criar_pedido(self):
        if not self.conta_id or len(self.produto_ids) < 2:
            return

        pedido_body = {
            "contaId": self.conta_id,
            "metodoPagamento": "CARTAO_CREDITO",
            "produtosIds": self.produto_ids
        }

        with self.client.post("/pedido", json=pedido_body, name="Criar Pedido", catch_response=True) as response:
            if response.status_code != 201:
                response.failure("Falha ao criar pedido")
