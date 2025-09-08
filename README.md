# Cheat Sheet de **Ollama** y uso de **DeepSeek** (Linux)

> Guía rápida para instalar, configurar y usar modelos locales con **Ollama** en Linux, con ejemplos listos para **DAW** y **DAM**.

---

## 🧩 Instalación en Linux (Ubuntu / Linux Lite)

### Opción 1 — Instalador oficial (recomendada)
```bash
curl -fsSL https://ollama.com/install.sh | bash
# Si usas systemd:
sudo systemctl enable ollama
sudo systemctl restart ollama
```

### Opción 2 — Binario manual (cuando no puedes usar curl | bash)
1) Descarga el binario de tu arquitectura desde la página oficial.  
2) Copia el ejecutable a `/usr/local/bin/ollama` y dale permisos `755`.  
3) Crea el servicio systemd (opcional) para iniciarlo al arrancar.

> Puerto por defecto de la API: **http://localhost:11434**

---

## 🧪 Verificación rápida
```bash
# Ver versión
ollama --version

# Descargar un modelo pequeño (rápido en CPU)
ollama pull llama3:8b

# Probar en modo interactivo
ollama run llama3:8b
```

> Con CPU como **Intel i3-10110U** empieza con modelos de **7B/8B** o variantes cuantizadas. Si notas lentitud, baja `num_ctx` (p. ej. 2048) o usa `q4` cuando esté disponible.

---

## 🧰 Comandos esenciales
```bash
# Descargar / actualizar modelo
ollama pull deepseek-r1:1.5b

# Listar modelos locales (ya descargados)
ollama list

# Ver detalles de un modelo
ollama show llama3:8b

# Ejecutar de forma interactiva (mantiene contexto en esa sesión)
ollama run deepseek-r1:1.5b

# Ejecutar con prompt de una sola vez (no hay contexto entre invocaciones)
echo "Explica la fotosíntesis" | ollama run llama3:8b

# Ver procesos / detener
ollama ps
ollama stop llama3:8b

# Eliminar un modelo
ollama rm llama3:8b
```

---

## 🌍 ¿Dónde ver más modelos disponibles?
👉 Catálogo oficial de modelos: [https://ollama.com/library](https://ollama.com/library)  

Allí encontrarás nombres, tamaños y variantes listos para usar.  
Algunos ejemplos:
- `llama3:8b`, `llama3:70b`
- `mistral:7b`
- `codellama:7b`
- `qwen2:7b`
- `deepseek-r1:1.5b`

Para instalarlos:
```bash
ollama pull nombre_del_modelo
```

---

## 🧠 ¿Qué es un `Modelfile`?
Archivo que define **cómo corre el modelo** (base + parámetros + sistema).

```dockerfile
FROM llama3:8b
SYSTEM "Eres un asistente útil y conciso que responde en español."
PARAMETER temperature 0.7
PARAMETER num_ctx 4096
```

Crear el modelo y usarlo:
```bash
ollama create nuevo-contexto -f Modelfile
ollama run nuevo-contexto
```

---

## 🔧 Parámetros útiles (resumen)
| Parámetro | Para qué sirve | Recomendación |
|---|---|---|
| `temperature` | Creatividad | 0.2–0.4 técnico, 0.7–1.0 creativo |
| `num_ctx` | Memoria de contexto | 2048–4096 en CPU modesta |
| `top_p` | Nucleus sampling | 0.8–0.95 |
| `top_k` | Tokens candidatos | 20–50 |
| `repeat_penalty` | Evitar repeticiones | 1.1–1.3 |
| `num_predict` | Máx. tokens de salida | 128–512 según caso |
| `stop` | Cortar salida | por ejemplo `###` |

---

## 🧵 Contexto (sesión vs. tuberías)
- **Interactivo** (`ollama run modelo`): mantiene contexto **hasta salir**.
- **Comando** (`echo | ollama run`): **no** mantiene contexto. Para simularlo, **reenvía el historial**.

```bash
# Historial acumulado
printf "Usuario: Explica la fotosíntesis.\n" > conversacion.txt
cat conversacion.txt | ollama run nuevo-contexto

# Añadir pregunta y reenviar
printf "Usuario: ¿Relación con la respiración celular?\n" >> conversacion.txt
cat conversacion.txt | ollama run nuevo-contexto
```

---

## 🧪 API HTTP (localhost:11434)
```bash
curl http://localhost:11434/api/generate \
  -d '{
    "model": "llama3:8b",
    "prompt": "Resume en 3 puntos la fotosíntesis.",
    "options": { "temperature": 0.4, "num_ctx": 4096, "num_predict": 160 }
  }'
```

---

## 🧠 DeepSeek en Ollama (siempre en español)
```dockerfile
FROM deepseek-r1:1.5b
PARAMETER temperature 0.3
PARAMETER num_ctx 8192
SYSTEM "Eres un asistente de IA. RESPONDE SIEMPRE EN ESPAÑOL, con claridad técnica."
```

### Presets por ciclo formativo
**DAW**
```dockerfile
FROM llama3:8b
SYSTEM "Mentor DAW. HTML5/CSS/JS(ES6+), Node/Express, REST/JSON, CORS/CSRF. Responde en español con ejemplos ejecutables."
PARAMETER temperature 0.5
PARAMETER num_ctx 4096
```

**DAM**
```dockerfile
FROM llama3:8b
SYSTEM "Mentor DAM. Java/Kotlin, Android, SQLite/Room, MVVM, pruebas, arquitectura limpia. Incluye snippets y comandos Gradle."
PARAMETER temperature 0.5
PARAMETER num_ctx 4096
```

---

## 🧩 Plantillas y JSON estructurado
```dockerfile
FROM llama3:8b
SYSTEM "Responde solo en JSON válido."
RESPONSE_FORMAT json
PARAMETER temperature 0.3
```

---

## ⚡ Rendimiento y solución de problemas
- **CPU lenta**: usa 7B/8B, baja `num_ctx` a 2048–3072 y `num_predict` a 128–256.
- **Salida se corta**: sube `num_predict`.
- **Repite mucho**: baja `temperature`, sube `repeat_penalty`.
- **GPU (opcional)**: si tienes NVIDIA/CUDA o AMD ROCm instalados, Ollama puede usarlos automáticamente.
- **Logs del servicio**: `journalctl -u ollama -f` (o alias `ollamalogs`).

---

## 📚 Enlaces útiles
- Catálogo de modelos: https://ollama.com/library
- Modelfile (docs): https://github.com/ollama/ollama/blob/main/docs/modelfile.md
- API: https://github.com/ollama/ollama/blob/main/docs/api.md
- Modelos populares: `llama3`, `mistral`, `qwen2`, `codellama`, `deepseek-r1`
