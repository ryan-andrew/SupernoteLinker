# Supernote Linker

#### Supernote Linker allows you to add links to your drawings from within your notes!

<img src="docs/videos/link.gif" alt="App Screenshot" width=500/>

## Linking Drawing

### Initial Link Setup Tutorial

There are some extra steps needed when setting up a link for the first time. You will need to set `Supernote Linker` as
the default handler for its links, and you will need to grant file permissions so that it can open the drawing files.
Here is a detailed breakdown of how to set up your first link!

1. Select something that you wish to have link to a drawing and tap to create a link.
    
    <img src="docs/screenshots/link/link_flow_1_circle_link.png" alt="App Screenshot" width=300/>

2. Choose `Web Page` from the list of `Link to` options.

    <img src="docs/screenshots/link/link_flow_2_link_settings.png" alt="App Screenshot" width=300/>

3. The box will already have `https://` entered. Just add `link/YOUR_CUSTOM_LINK_ID` to it. This link ID can have any
text you want in it. This link will be able to be reused if you wish to use it in other places as well, just use the
same link ID!

    <img src="docs/screenshots/link/link_flow_3_edit_link.png" alt="App Screenshot" width=300/>

4. Here is an example: `https://link/sketch123`

    <img src="docs/screenshots/link/link_flow_4_finish_entering_link.png" alt="App Screenshot" width=300/>

5. The link is now created, and you will be taken back to your note. Now tap on your newly created link. Choose to open
with `Supernote Linker` Always. You won't have to see this popup again. This tells Android to send all links that
start with `link/` to `Supernote Linker`. No real websites can ever start with this, so there are no conflicts with
your web browser.

    <img src="docs/screenshots/link/link_flow_5_set_default.png" alt="App Screenshot" width=300/>

6. The first time you use `Supernote Linker`, you will need to grant file permissions. This is so that the app can
access the drawing files in order to open them. Tap `Grant Permission`.

    <img src="docs/screenshots/link/link_flow_6_permission_screen.png" alt="App Screenshot" width=300/>

7. You will be taken to the Android settings page. Ensure that `Allow access to manage all files` is enabled, then
press the back button at the top. You will only need to do this once.

    <img src="docs/screenshots/link/link_flow_7_allow_access.png" alt="App Screenshot" width=300/>

8. The file picker will now open. From now on, this will be the first screen you see when creating new links, as
permissions have now been granted.

    <img src="docs/screenshots/link/link_flow_8_file_chooser.png" alt="App Screenshot" width=300/>

9. Select whichever drawing you want to link to. Only drawing files (and folders that contain drawing files) will be
shown.

    <img src="docs/screenshots/link/link_flow_9_file_chooser_2.png" alt="App Screenshot" width=300/>

10. That's it! Now the link in your note file will open your drawing!

    <img src="docs/videos/link.gif" alt="App Screenshot" width=300/>


## Creating Popup Messages

Another feature of `Supernote Linker` is the ability to create a link that shows a popup message. To create a popup
link, do the following:
1. Create a link
2. Make the web address `https://popup/WHATEVER MESSAGE YOU WANT HERE!`


<img src="docs/screenshots/popup/popup_edit_link.png" alt="App Screenshot" width=300/>
<img src="docs/screenshots/popup/popup_tap_link.png" alt="App Screenshot" width=300/>

### In Action

<img src="docs/videos/popup.gif" alt="App Screenshot" width=500/>
