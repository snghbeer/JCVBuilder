<script setup>
import { ref, watch, watchEffect } from 'vue';
import AddBtn from './addButton.vue'
import SkillSectionComponent from './SkillSectionComponent.vue';

const props = defineProps({
    skillSectionList: Array,
    update: Function
});

// Function to add a new experience to the list
const addSkillSection = () => {
    props.skillSectionList.push({
    title: 'New section',
    skills: [],
  });
};

//Debug: update in parent

const handleTitleEdit = (index, newTitle) => {
    //props.skillSectionList[index].title = newTitle;
    props.update(index, newTitle);

};




</script>

<template>
    <div class="skillContainer">
        <SkillSectionComponent v-for="(section, index) in props.skillSectionList" :key="index" :update="handleTitleEdit" :section="{...section, index}"/>
    </div>
    <div class="flex-Center">
        <AddBtn @click="addSkillSection" />
    </div>

</template>

<style scoped>

.skillContainer{
    padding: 3.5rem;
    overflow-y: auto;
    max-height: 20vh;
}

</style>