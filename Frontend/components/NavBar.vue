<template>
    <div class="navbar">
        <span v-if="currentFolder !== 'Contacts'" class="pi pi-refresh refresh" @click.stop="this.$emit('open')"></span>
        <span style="position: absolute; left: 3vw;">
            <button class="compose" :class="{ selected: composing }" @click="this.$emit('compose')">
                <i class="pi pi-pencil icon" style="position: absolute;left: 0.7vw;font-size: 2.5vh;margin-top: 0.2vh;"></i>
                Compose
            </button>
        </span>
        <span v-if="currentFolder !== 'Contacts'" style="position: absolute;left: 20vw;margin-top: 0.2vh;">
            <button class="pi pi-check-square selecttoggle" @click="this.$emit('toggleMultiSelect')"></button>
            <span v-if="multiselect">
                <button class="pi pi-trash selectoptnbtn" @click="this.$emit('deleteEmail')"></button>
                <button class="pi pi-folder-open selectoptnbtn" @click="this.$emit('moveEmail')"></button>
            </span>
        </span>
        <span class="search-container">
            <input class="search-field" type="text" v-model="value" @input="search" placeholder="Search" />
        </span>
        <div v-if="currentFolder !== 'Contacts'" class="multiselect">
            <MultiSelect v-model="selectedFilters" :options="filterOptions" placeholder="Select Filters"
                :maxSelectedLabels="3" @change="search"></MultiSelect>
        </div>
        <span v-if="currentFolder !== 'Contacts'" class="sortOptns">
            <button :class="{ selected: sortOption === 'date' }" class="sortOptn defaultbtn"
                @click="sort('date')">Default</button>
            <button :class="{ selected: sortOption === 'priority' }" class="sortOptn prioritybtn"
                @click="sort('priority')">Priority</button>
        </span>
        <span>
            <button class="sortOrder" @click="sortOrdr()">
                <i v-if="sortOrder === 'descending'" class="pi pi-sort-amount-down"></i>
                <i v-else class="pi pi-sort-amount-up"></i>
            </button>
        </span>
    </div>
</template>

<script>
import 'primeicons/primeicons.css';
import 'primevue/resources/themes/mira/theme.css';
import 'primevue/resources/themes/mira/theme.css';
// import 'primevue/resources/themes/md-dark-indigo/theme.css';
import MultiSelect from 'primevue/multiselect';

export default {
    name: 'NavBar',
    components: { MultiSelect },
    data() {
        return {
            value: '',
            items: ['Youssif', 'Ahmed', 'Mohamed'],
            searchResults: [],
            sortOption: 'date',
            sortOrder: 'descending',
            selectedFilters: [],
            filterOptions: ['Sender', 'Receiver', 'Subject', 'Body'],
        };
    },
    props: ['currentFolder', 'multiselect', 'composing'],
    methods: {
        sort(option) {
            this.sortOption = option;
            this.$emit('sort', this.sortOption, this.sortOrder);
        },
        sortOrdr() {
            if (this.sortOrder === 'descending') {
                this.sortOrder = 'ascending';
            } else {
                this.sortOrder = 'descending';
            }
            this.$emit('sort', this.sortOption, this.sortOrder);
        },
        search(){
            console.log(this.value);
            this.$emit('search', this.value, this.selectedFilters);
        },
    },
};
</script>

<style scoped>
.navbar {
    position: relative;
    background-color: rgba(7, 10, 10, 0.821);
    height: 5.7vh;
    width: 100vw;
    margin: 0;
    border-bottom: 0.1vw solid black;
    border-top: 0.1vw solid black;
}

.refresh {
    position: absolute;
    left: 2vh;
    top: 1vh;
    font-size: 3vh;
    background: none;
    border: none;
    color: black;
    font-weight: bold;
    cursor: pointer;
}

.compose {
    color: aliceblue;
    margin-top: 0.7vh;
    width: 12vw;
    border-radius: 1vw;
    cursor: pointer;
    background-color: rgba(32, 30, 30, 0.863);
    border: 0.1vw solid black;
    font-size: 3vh;
}

.compose.selected {
    color: rgb(78, 77, 76);
    background-color: rgba(22, 20, 20, 0.863);
}

.compose:hover {
    background-color: rgba(22, 20, 20, 0.863);
}

.selecttoggle {
    border-radius: 0.8vw;
    background: none;
    padding: 0.5vw;
    color: white;
    cursor: pointer;
    font-size: 2.5vh;
}

.selectoptnbtn {
    background-color: #b9b9b9;
    border-radius: 0.8vw;
    padding: 0.4vw;
    color: rgb(0, 0, 0);
    cursor: pointer;
    font-size: 2vh;
    width: 2.5vw;
}

.selecttoggle:hover {
    background-color: rgba(22, 20, 20, 0.863);
}

.selectoptnbtn:hover {
    background-color: rgba(22, 20, 20, 0.863);
    color: white;
}

.search-container {
    margin-top: 1vh;
    width: 35vw;
    position: relative;
    display: inline-block;
}

.search-field {
    width: 60%;
    padding: 0.5vh 0.5vw;
    border-radius: 10px;
    border: 0.1vw solid black;
    font-size: 2vh;
    background-color: rgba(32, 30, 30, 0.863);
    color: aliceblue;
}

.icon {
    font-size: 1.1vw;
}

.refresh:hover {
    color: white;
}

.sortOptns {
    position: absolute;
    right: 3vw;
    margin-top: 0.7vh;
}

.sortOptn {
    background-color: #b9b9b9;
    padding: 0.4vw;
    color: rgb(0, 0, 0);
    cursor: pointer;
    font-size: 1.5vh;
    font-weight: bold;
    width: 4vw;
    height: 4vh;
}

.selected {
    background-color: rgba(22, 20, 20, 0.863);
    color: white;
}

.defaultbtn {
    border-top-left-radius: 10px;
    border-bottom-left-radius: 10px;
}

.prioritybtn {
    border-top-right-radius: 10px;
    border-bottom-right-radius: 10px;
}

.sortOrder {
    background-color: #b9b9b9;
    color: rgb(0, 0, 0);
    cursor: pointer;
    width: 1.9vw;
    height: 3.5vh;
    border-radius: 1vh;
    margin: 1vh;
    position: absolute;
    right: 0.5vw;
}

.multiselect {
    display: inline-block;
    height: 25px;
    width: 10px;
    
}
</style>
