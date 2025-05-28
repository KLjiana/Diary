## 📖 Diary Mod – Leave Your Mark in the World of Minecraft

**Diary Mod** brings a complete journaling system to the world of Minecraft, allowing you to read, collect, display, and
write beautifully styled diary entries. Whether you're documenting your adventures, writing serialized novels, or
decorating with cute little illustrations, this mod makes it all effortless.

---

### ✨ Key Gameplay Features

* **Page-Turning Reading Interface**
  Right-click a “Diary Paper” or “Diary” item to open a realistic book-style GUI. Flip through the pages and click the
  “Done” button for a seamless, immersive reading experience.

* **Randomized Paper and Cover Styles**
  Each Diary Paper item in your inventory is assigned a random `CustomModelData` value (1–3), giving it a unique
  appearance—from aged parchment full of writing to a kawaii scrapbook covered in stickers.
  Diary covers also come in various styles, making your diary truly one-of-a-kind.

* **Unlimited Volumes and Chapters**
  A single Diary can be made up of multiple Diary Papers. When you reach the last page of one, it automatically jumps to
  the next volume, keeping chapters clearly organized. You can title each volume and write subtitles or dates on the
  cover page to give your story that special touch.

* **Creative Mode Integration**
  In Creative Mode, all registered Diary Papers and Diaries are neatly listed under the “Diary” tab—making it easy for
  server admins and map makers to preview, pick, and test content quickly.

* **Fully Data-Driven Content**
  Everything—from page text to book titles and chapter order—is driven by JSON files in the resource pack. You don’t
  need to write a single line of code to provide players with brand-new diary themes:

    * **Adventure Logs**: Chronicle your fortress raids or End explorations
    * **Creature Compendium**: Document fantastical beasts with sketches and notes
    * **Story Integration**: Tie custom quests and dialogue scripts together through immersive diary entries

---

### 🎲 Example: How to Use in Game

1. **Get the Items**
- `/give @s diary_paper{diary_id:"mystic_journal"} 1`
- `/give @s diary{diary_type:"mystic_journal"} 1`

2. **Open to Read**
- Right-click while holding a Diary Paper or Diary item to open the GUI.

3. **Flip Pages / Exit**
- Use the arrow buttons to flip pages. Click “Done” or press Esc to close.

4. **Decorate and Display**
- Screenshot or record the reading interface for adventure log walls on your server, or create an exhibit area to
  showcase your journals.

---

### 🛠 Expandability

- **No Restart Needed**: Just add or modify files under `data/diary/paper/*.json` and `data/diary/book/*.json`, then
  reload with `/reload` to see changes instantly.
- **Create Your Own**: Map makers and mod creators can define new diary themes, chapters, and text using simple JSON
  files.
- **Open API**: Can be integrated with other mods to trigger events, play sounds, or interact with custom quest systems
  when diary items are used.

---

Let every adventure leave a written legacy—use the **Diary Mod** to weave your stories into beautifully crafted journal
entries!
