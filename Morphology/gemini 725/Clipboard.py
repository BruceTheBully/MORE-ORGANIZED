import time
import pyperclip

# File path to save clipboard content
file_path = "L:\Python Morph\Markdown.txt"

# Store the last copied text
last_text = ""

# Main loop to monitor clipboard
while True:
    try:
        # Get current clipboard content
        current_text = pyperclip.paste()

        # If new content is found, append to file
        if current_text != last_text:
            with open(file_path, "a", encoding="utf-8") as file:
                file.write(current_text + "\n\n" + "-"*80 + "\n\n")
            last_text = current_text
            print("New clipboard content saved.")
        
        # Delay between checks
        time.sleep(1)

    except KeyboardInterrupt:
        print("Stopped by user.")
        break
    except Exception as e:
        print(f"Error: {e}")
        break
