<template>
    <div class="sidebar">
        <button :class="{ selected: currentFolder === 'Contacts' }" @click="$emit('openContacts')" class="sidebar-button"><i
                class="pi pi-users icon"></i>Contacts</button>
        <i style="border-bottom: 0.1vw solid black;border-top: 0.1vw solid black ;padding-bottom: 1.5vh;padding: 0;">
            <button :class="{ selected: currentFolder === 'Inbox' }" @click="$emit('open', 'Inbox')"
                class="sidebar-button"><i class="pi pi-inbox icon"></i>Inbox</button>
            <button :class="{ selected: currentFolder === 'Sent' }" @click="$emit('open', 'Sent')" class="sidebar-button"><i
                    class="pi pi-send icon"></i>Sent</button>
            <button :class="{ selected: currentFolder === 'Draft' }" @click="$emit('open', 'Draft')"
                class="sidebar-button"><i class="pi pi-clock icon"></i>Draft</button>
            <button :class="{ selected: currentFolder === 'Trash' }" @click="$emit('open', 'Trash')"
                class="sidebar-button"><i class="pi pi-trash icon"></i>Trash</button>
        </i>
        <div class="userFolders">
            <button v-for="folder in Addedfolders" :key="folder" class="userFolder" @click="$emit('open', folder.name)">
                <div class="folderName">
                    {{ folder.name }}
                </div>
                <div class="folderOptions">
                    <i class="pi pi-trash deleteFolder icon" @click.stop="deleteFolder(folder.name)"></i>
                    <i class="pi pi-file-edit renameFolder icon" @click.stop="RenameFolder(folder.name)"></i>
                </div>
            </button>
        </div>
        <button class="sidebar-button add-button" @click="addFolder"><i class="pi pi-plus-circle  icon"></i>
            Add Folder
        </button>
    </div>
</template>

<script>
export default {
    name: 'SideBar',
    props: ['currentFolder', 'useremail', 'addedFolder', 'Folders'],
    data() {
        return {
            Addedfolders: this.Folders,
        };
    },
    watch: {
        addedFolder: function (val) {
            if (!this.Addedfolders.find(folder => folder.name === val)) {
                this.Addedfolders.push({ name: val });
            }
        },
        Folders: function (newValue) {
            this.Addedfolders = newValue
        }
    },
    methods: {
        addFolder() {
            let folder = {
                name: prompt("Please enter folder name", "New Folder"),
            };
            let baseName = folder.name;
            let i = 1;
            if (folder.name === null) {
                return;
            }
            while (this.Addedfolders.find(f => f.name === folder.name)) {
                folder.name = `${baseName}(${i})`;
                i++;
            }
            this.Addedfolders.push({ ...folder });
            console.log(this.useremail)
            fetch('http://localhost:8080/folder/' + this.useremail + "/" + folder.name, {
                method: "POST",
            }).then(
                console.log("Folder created successfully")
            )
        },
        deleteFolder(name) {
            this.Addedfolders = this.Addedfolders.filter(folder => folder.name !== name);
            console.log(this.useremail)
            console.log(name)
            fetch('http://localhost:8080/folder/' + this.useremail + "/" + name, {
                method: "DELETE",
            }).then(
                console.log("Folder Deleted successfully")
            )
        },
        RenameFolder(name) {
            let folder = this.Addedfolders.find(folder => folder.name === name);
            let newName = prompt("Please enter new folder name", folder.name);
            let baseName = newName;
            let i = 1;
            while (this.Addedfolders.find(f => f.name === newName)) {
                newName = `${baseName}(${i})`;
                i++;
            }
            fetch('http://localhost:8080/folder/' + this.useremail + '/' + name + '/' + newName, {
                method: "POST",
            }).then(
                console.log("Folder renamed successfully")
            )
            folder.name = newName;
        }
    },
}
</script>

<style scoped>
.sidebar {
    background-color: rgba(7, 10, 10, 0.821);
    height: 85.1vh;
    width: 20vw;
    margin: 0;
    border-right: 0.1vw solid black;
    border-bottom: 0.1vw solid black;
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-items: center;
}

.sidebar-button {
    padding: 2vh;
    width: 100%;
    background-color: transparent;
    border-top-right-radius: 30px;
    border-bottom-right-radius: 30px;
    border: none;
    color: white;
    font-size: 1.2vw;
    text-align: left;
    cursor: pointer;
}

.selected {
    background-color: rgba(7, 10, 10, 0.531);
    color: black;
}

.sidebar-button.selected:hover {
    background-color: rgba(7, 10, 10, 0.531);
    color: black;
}

.sidebar-button:hover {
    background-color: rgba(7, 10, 10, 0.215);
    color: black;
}

.userFolders {
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-items: center;
    width: 100%;
    height: 100%;
    margin-top: 2;
    overflow-y: scroll;
}

::-webkit-scrollbar {
    width: 0.8vw;
    background-color: #222121;
    /* Dark background */
    border-radius: 5px;
}

::-webkit-scrollbar-thumb {
    background-color: #888;
    /* Lighter handle */
    border-radius: 5px;
}

.userFolder {
    width: 100%;
    background-color: transparent;
    padding: 1vh;
    height: 5vh;
    border-top-right-radius: 30px;
    border-bottom-right-radius: 30px;
    border: none;
    color: white;
    font-size: 1.2vw;
    text-align: left;
    cursor: pointer;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
}

.userFolder:hover {
    background-color: rgba(7, 10, 10, 0.215);
    color: black;
}

.folderName {
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    align-items: center;
    width: 17vw;
    overflow: hidden;
    white-space: nowrap;
}

.userFolder:hover .folderName {
    width: 12vw;
}

.folderOptions {
    position: relative;
    left: 0.2vw;
    display: flex;
    flex-direction: row;
    justify-content: flex-end;
    align-items: center;
}

.deleteFolder:hover {
    color: red;
}

.renameFolder:hover {
    color: green;
}

.userFolder .folderOptions {
    display: none;
}

.userFolder:hover .folderOptions {
    display: block;
}

.add-button {
    margin-top: auto;
    margin-bottom: 10px;
    font-weight: bold;
}

.icon {
    margin-right: 1vw;
    font-size: 1.2vw;
}
</style>