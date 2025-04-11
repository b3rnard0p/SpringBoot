import json
import mysql.connector
from mysql.connector import Error

def import_json_to_mysql(json_file_path, db_config):
    """
    Importa dados de um arquivo JSON para o banco MySQL
    
    :param json_file_path: Caminho para o arquivo JSON
    :param db_config: Dicionário com configurações do banco
    """
    try:
        # 1. Ler o arquivo JSON
        with open(json_file_path, 'r', encoding='utf-8') as file:
            users_data = json.load(file)
        
        # 2. Conectar ao banco de dados
        conn = mysql.connector.connect(**db_config)
        cursor = conn.cursor()
        
        # 3. Inserir dados
        total_users = 0
        total_phones = 0
        
        for user in users_data:
            # Corrigir nome se estiver com erro
            name = user['name']
            if isinstance(name, str) and name.startswith('<ReferenceError'):
                # Extrai nome do email (parte antes do @)
                name = user['email'].split('@')[0]
                # Remove pontos e capitaliza
                name = name.replace('.', ' ').title()
            
            # Inserir usuário
            cursor.execute(
                "INSERT INTO user (email, nome, senha) VALUES (%s, %s, %s)",
                (user['email'], name, 'senha_temp')  # Senha padrão, pode modificar
            )
            user_id = cursor.lastrowid
            total_users += 1
            
            # Inserir telefones
            for phone in user['phones']:
                cursor.execute(
                    "INSERT INTO phone (user, number) VALUES (%s, %s)",
                    (user_id, phone['number'])
                )
                total_phones += 1
        
        # 4. Confirmar as alterações
        conn.commit()
        print(f"Importação concluída com sucesso!")
        print(f"Total de usuários inseridos: {total_users}")
        print(f"Total de telefones inseridos: {total_phones}")
        
    except json.JSONDecodeError as e:
        print(f"Erro ao ler o arquivo JSON: {e}")
    except Error as e:
        print(f"Erro no banco de dados MySQL: {e}")
    except Exception as e:
        print(f"Erro inesperado: {e}")
    finally:
        if 'conn' in locals() and conn.is_connected():
            cursor.close()
            conn.close()

# Configuração do banco de dados (use suas credenciais)
db_config = {
    'host': 'localhost',
    'database': 'UserPhone',
    'user': 'root',
    'password': 'senha'
}

# Executar a importação
import_json_to_mysql('generated.json', db_config)