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
## 🔧 Parámetros de `Modelfile` en Ollama

| Parámetro        | Descripción                                                                 | Instrucciones / Cuándo usarlo                                                                 | Ejemplo |
|------------------|-----------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------|---------|
| `temperature`    | Controla la **creatividad** de las respuestas.<br>0 = determinista, >1 = más creativo | Usa valores bajos (0.2–0.4) para precisión técnica.<br>Valores altos (0.8–1.2) para creatividad o brainstorming. | `PARAMETER temperature 0.8` |
| `num_ctx`        | Tamaño del **contexto** (tokens que el modelo puede recordar).              | Aumenta si trabajas con documentos largos o código extenso.<br>Por defecto ~4096 tokens.       | `PARAMETER num_ctx 8192` |
| `top_p`          | **Nucleus sampling**: limita la selección a los tokens más probables (p).   | Recomendado entre 0.8–0.95 para equilibrio entre precisión y diversidad.                      | `PARAMETER top_p 0.9` |
| `top_k`          | Selección de los **K tokens más probables**.                               | Valores bajos → más determinista.<br>Valores altos → más creativo.                            | `PARAMETER top_k 40` |
| `repeat_penalty` | Penaliza repeticiones de palabras/tokens.                                  | Útil si el modelo repite frases. Valores entre 1.1–1.3 son comunes.                           | `PARAMETER repeat_penalty 1.2` |
| `stop`           | Define tokens de **parada** (detiene la generación).                       | Muy útil para delimitar respuestas o cortar en un prompt específico.                          | `PARAMETER stop "###"` |
| `num_predict`    | Máximo de **tokens generados** por respuesta.                              | Ajusta para limitar la longitud de la salida. Ej: 200 tokens ≈ párrafo corto.                 | `PARAMETER num_predict 200` |


## 🧵 Crear un hilo de conversación con contexto

En Ollama, un **hilo de conversación** significa que el modelo recuerda lo que se habló antes para dar continuidad a las respuestas.  
Esto se logra configurando adecuadamente el `Modelfile` y usando el parámetro **`num_ctx`**, que define cuántos tokens (palabras + símbolos) puede recordar el modelo.

---

### Ejemplo de parámetros

Imagina que el modelo es como un estudiante tomando apuntes en clase:  
- **`num_ctx`** es el tamaño del cuaderno → mientras más grande, más puede recordar.  
- **El historial de conversación** son los apuntes previos → el modelo los usa para responder sin olvidar lo dicho antes.  
- Si el cuaderno es pequeño (`num_ctx` bajo), el modelo olvidará lo que escribiste al inicio.  
- Si es grande (`num_ctx` alto), podrá recordar todo el hilo de conversación.

---

### Cómo usarlo en la práctica
## 1. Crear el archivo Modelfile con el contenido anterior:
```
nano Modelfile
```
## 2. Crear el modelo personalizado:
```
ollama create nuevo-contexto -f Modelfile
```
nuevo-contexto es el nombre del modelo personalizado

## 3. Ejecutarlo e iniciar la conversación:
```
ollama run nuevo-contexto
```

## 4. Ejemplo de diálogo en Linux con `cat` y tuberías**

### Opción A — Una sola pregunta (sin contexto entre runs)
```bash
echo "Explícame qué es la fotosíntesis." > pregunta1.txt
```

En Linux, podemos simular un diálogo creando archivos de texto y enviándolos al modelo con `cat` y el operador `|`.

---
### Opción B — Simular contexto enviando el historial acumulado
Como ollama run es stateless entre ejecuciones, reenvía el histórico completo.
1. Crea un archivo con el histórico:
```bash
cat > conversacion.txt << 'EOF'
Usuario: Explica qué es la fotosíntesis.
EOF
```
2. Envíalo al modelo:
```bash
cat conversacion.txt | ollama run nuevo-contexto
```

3. Añade una nueva pregunta al mismo archivo y vuelve a enviarlo:
```bash
cat >> conversacion.txt << 'EOF'
Usuario: ¿Y cómo se relaciona con la respiración celular?
EOF

cat conversacion.txt | ollama run nuevo-contexto
```

### Opción C — Dentro de una sesión (recomendado)
```bash
ollama run nuevo-contexto
# ahora pregunta, recibe respuesta, y vuelve a preguntar SIN salir
```


# Otros ejemplos de Modelfile por caso de uso
## DeepSeek en Ollama (responder siempre en español)

```Dockerfile
FROM deepseek-r1:1.5b
PARAMETER temperature 0.3
PARAMETER num_ctx 8192
SYSTEM "Eres un asistente de IA. RESPONDE SIEMPRE EN ESPAÑOL, con claridad técnica."
```

## 🧩 Otro ejemplos de Modelfile por caso de uso

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


##  📚 Estilo Profesor (explicaciones largas y detalladas)
```Dockerfile
FROM llama2
SYSTEM """
Eres un profesor paciente. Explica de forma clara y con ejemplos detallados.
"""
PARAMETER temperature 0.8
PARAMETER num_ctx 8192

```

## 🧰 Comandos básicos de Ollama

```bash
# Descargar un modelo
ollama pull llama3:8b

# Ejecutar un modelo en modo interactivo (mantiene contexto en esa sesión)
ollama run llama3:8b

# Ejecutar con prompt directo (una sola vez)
echo "Explica la fotosíntesis" | ollama run llama3:8b

# Listar modelos locales
ollama list

# Ver procesos en ejecución
ollama ps

# Detener un proceso por nombre o ID
ollama stop llama3:8b

# Eliminar un modelo local
ollama rm llama3:8b

# Crear un modelo desde un Modelfile
ollama create mi-modelo -f Modelfile



```markdown
## 🧭 Elegir modelo y variantes

- **Nombres comunes**: `llama3:8b`, `llama3:70b`, `mistral:7b`, `codellama:7b`, `qwen2:7b`, `deepseek-r1:1.5b`.
- **Tamaño**: 7B/8B = rápido y ligero; 13B–70B = mejor calidad pero más memoria.
- **Cuantización**: variantes más “ligeras” consumen menos RAM (p. ej. etiquetas `:q4`/`q5` según el modelo).
- **Consejo**: empieza con 7B/8B; si necesitas más calidad, sube gradualmente.

## 🧵 Contexto: sesión vs. tuberías

- **Interactivo** (`ollama run modelo`) ⇒ **sí** mantiene el contexto hasta que sales.
- **Comando único** (`echo "..." | ollama run modelo`) ⇒ **no** mantiene contexto entre invocaciones.
- Para “simular” contexto con tuberías, **reenvía el historial completo** cada vez:

```bash
# Crear historial
printf "Usuario: Explica la fotosíntesis.\n" > conversacion.txt
cat conversacion.txt | ollama run nuevo-contexto

# Añadir nueva pregunta
printf "Usuario: ¿Relación con la respiración celular?\n" >> conversacion.txt
cat conversacion.txt | ollama run nuevo-contexto


```markdown
## 🧪 API HTTP de Ollama (localhost:11434)

> Útil para apps, scripts y automatización.

### Generación (texto único)
```bash
curl http://localhost:11434/api/generate \
  -d '{
    "model": "llama3:8b",
    "prompt": "Resume en 3 puntos la fotosíntesis.",
    "options": { "temperature": 0.4, "num_ctx": 4096, "num_predict": 160 }
  }'
```



## 🧪 API HTTP de Ollama (localhost:11434)

> Útil para apps, scripts y automatización.

### Generación (texto único)
```bash
curl http://localhost:11434/api/generate \
  -d '{
    "model": "llama3:8b",
    "prompt": "Resume en 3 puntos la fotosíntesis.",
    "options": { "temperature": 0.4, "num_ctx": 4096, "num_predict": 160 }
  }'
```


```markdown
## 🧩 Plantillas y salida en JSON

### Forzar JSON estructurado
```Dockerfile
FROM llama3:8b
SYSTEM "Responde solo en JSON válido."
RESPONSE_FORMAT json
PARAMETER temperature 0.3
```


```markdown
## ➕ Parámetros adicionales útiles

| Parámetro            | Para qué sirve                                | Ejemplo                          |
|---------------------|-----------------------------------------------|----------------------------------|
| `seed`              | Fijar aleatoriedad (reproducible)             | `PARAMETER seed 42`              |
| `presence_penalty`  | Evita repetir **temas**                        | `PARAMETER presence_penalty 0.8` |
| `frequency_penalty` | Evita repetir **palabras**                     | `PARAMETER frequency_penalty 0.5`|
| `mirostat`          | Control de coherencia (0=off, 1 ó 2 = on)     | `PARAMETER mirostat 2`           |
| `mirostat_tau`      | Temperatura objetivo de Mirostat               | `PARAMETER mirostat_tau 5.0`     |
| `mirostat_eta`      | Velocidad de ajuste Mirostat                   | `PARAMETER mirostat_eta 0.1`     |

## ⚡ Rendimiento y solución de problemas

- **Memoria insuficiente**: usa modelos más pequeños (7B/8B) o cuantizados, baja `num_ctx`.
- **Salida se corta**: sube `num_predict`.
- **Respuestas muy largas/repetitivas**: baja `temperature`, sube `repeat_penalty`.
- **GPU**: asegúrate de tener drivers y CUDA/Metal según tu SO (Ollama lo detecta automáticamente si es posible).
- **Lento en CPU**: reduce `top_k`, `num_ctx`, o usa variantes cuantizadas.

## 🧠 DeepSeek (nota y ejemplos)

- **Local** con Ollama: `FROM deepseek-r1:1.5b` en tu `Modelfile`.
- **En la nube** (API oficial) ⇒ requiere **API key** y conexión a Internet.

### Python (cliente ficticio como ejemplo pedagógico)
```python
from deepseek import DeepSeek
ds = DeepSeek(api_key="TU_API_KEY")
print(ds.chat("Explica aprendizaje profundo en 3 líneas."))


```markdown
## 🎯 Presets por ciclo formativo

### DAW — Desarrollo de Aplicaciones Web
```Dockerfile
FROM llama3:8b
SYSTEM "Mentor DAW. HTML5/CSS/JS(ES6+), Node/Express, REST/JSON, CORS/CSRF. Responde en español con ejemplos ejecutables."
PARAMETER temperature 0.5
PARAMETER num_ctx 8192
```

```Dockerfile
FROM llama3:8b
SYSTEM "Mentor DAM. Java/Kotlin, Android, SQLite/Room, MVVM, tests, arquitectura limpia. Incluye snippets y comandos Gradle."
PARAMETER temperature 0.5
PARAMETER num_ctx 8192
```


