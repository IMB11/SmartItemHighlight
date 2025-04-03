# Smart Item Highlight

Smartly highlight items in your inventory based on their properties such as enchantments, various components or even if you've recently picked it up.

By default, Smart Item Highlight will only highlight items if they've been recently picked up, mimicking the logic of the existing [Item Highlighter](https://modrinth.com/mod/item-highlighter) mod. You can however access the configuration screen of Smart Item Highlight via Mod Menu or the mods list on NeoForge to create new highlight conditions.

## Highlight Conditions

Highlight Conditions are incredibly powerful, they allow you to highlight items in your inventory based on a range of conditions. There are tons of examples in the `/config/smartitemhighlight/conditions/examples` folder of your game instance. Easily configure them via the configuration screen:



## Render Functions

When an item can be highlighted in the inventory the highlight condition's render function will be called, these render functions are highly configurable, simply provide a `renderFunctionOptions`

## Contributing

_Whilst this project is All Rights Reserved, you may fork to add translations or new features. Any merged pull requests will immediately fall under the ownership of IMB11 (Calum H.)_

Open the project within IntellIJ IDEA for the best experience, the stonecutter system has been known to cause issues with IDEs such as Eclipse and VSCode. Alternatively you can just use GitHub's web interface if you're contributing translations.

Once you've got the project **fully imported** you can use the "Set active project to xxxx" tasks to switch between fabric, neoforge and other Minecraft versions.

When you are finished with your changes, **please run the `"Reset Active Project"` and `chiseledBuild` tasks before making a commit!**

## Credits

Thank you to Jack (jacks-underscore-username) and Bluuga (Hurricane) for working on this project with me!

- [Stonecutter by KikuGie](https://stonecutter.kikugie.dev/) for multiversioning support.
- [Modstitch by isXander](https://github.com/isXander/Modstitch) for single sourceset multiloader support alongside stonecutter.