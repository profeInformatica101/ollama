# Cheat Sheet de Ollama y uso de DeepSeek

Este documento sirve como una guía rápida para el uso de **Ollama** y **DeepSeek**.

---

## 🚀 Ollama

### Instalación
# 🧠 Ollama Cheat Sheet + Ejemplos de Modelfile

📘 **Documentación oficial:**  
https://github.com/ollama/ollama/blob/main/docs/modelfile.md  

---

# 🚀 ¿Qué es un `Modelfile`?

Es un archivo que define cómo se ejecuta un modelo en **Ollama**.  
Permite configurar instrucciones, parámetros de rendimiento y comportamiento.  

Un `Modelfile` se compone de:

- **FROM** → Modelo base  
- **PARAMETER** → Ajustes del modelo  
- **SYSTEM / TEMPLATE / RESPONSE_FORMAT** → Instrucciones y comportamiento  
- **LICENSE / TEMPLATES / ADAPTERS** → Opcional para casos avanzados  

---

# ⚙️ Ejemplo Básico de `Modelfile`

```Dockerfile
FROM llama2

# Mensaje inicial del sistema (instrucciones fijas)
SYSTEM """
Eres un asistente útil y conciso que responde en español.
"""

# Parámetros comunes
PARAMETER temperature 0.7
PARAMETER num_ctx 4096

```

| Parámetro        | ¿Qué hace?                                                    | Ejemplo                        |

| ---------------- | ------------------------------------------------------------- | ------------------------------ |

| `temperature`    | Controla la creatividad (0 = determinista, 1+ = más creativo) | `PARAMETER temperature 0.2`    |

| `num_ctx`        | Tamaño del contexto (tokens máximos recordados)               | `PARAMETER num_ctx 8192`       |

| `top_p`          | Nucleus sampling: restringe la salida a tokens más probables  | `PARAMETER top_p 0.9`          |

| `top_k`          | Selecciona tokens entre los K más probables                   | `PARAMETER top_k 40`           |

| `repeat_penalty` | Penaliza repeticiones de tokens                               | `PARAMETER repeat_penalty 1.2` |

| `stop`           | Define tokens de parada                                       | `PARAMETER stop "###"`         |

| `num_predict`    | Límite máximo de tokens generados en una respuesta            | `PARAMETER num_predict 200`    |

# 🧩 Ejemplos de Modelfile por caso de uso

```Dockerfile
FROM deepseek-r1:1.5b

# Parámetros
PARAMETER temperature 0.3
#PARAMETER num_predict 200

# Instrucciones iniciales: idioma español obligatorio
SYSTEM """
Eres un asistente de inteligencia artificial. 
RESPONDE SIEMPRE EN ESPAÑOL, sin utilizar nunca el inglés. 
Todas las respuestas deben ser claras, técnicas y adaptadas a usuarios hispanohablantes.
"""
```


## 1. 📚 Estilo Profesor (explicaciones largas y detalladas)
```Dockerfile
FROM llama2
SYSTEM """
Eres un profesor paciente. Explica de forma clara y con ejemplos detallados.
"""
PARAMETER temperature 0.8
PARAMETER num_ctx 8192

```

