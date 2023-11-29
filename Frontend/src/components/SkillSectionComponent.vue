<script setup>
import { ref, toRefs } from 'vue';
import SkillInput from './SkillInput.vue';
import EditableHeader from './EditableHeader.vue'

const props = defineProps({
    section: Object,
    update: Function
});

const { title, skills, index } = toRefs(props.section);

// Function to add a new experience to the list
const addSkill = () => {
    skills.value.push({
        skill: '',
        level: '',
    });
};

// Function to handle section title editing
 const handleTitleEdit = (index, newTitle) => {
  props.update(index, newTitle);
}; 




</script>

<template>
    <div class="skillSectionContainer">
        <!-- <h3 class="sectionHeaderInput" contentEditable @input="handleTitleEdit">
            {{ `${title} ${index + 1}` }}
        </h3>   -->      
        <EditableHeader :title="title" :index="index" @update:title="handleTitleEdit"></EditableHeader>

        <SkillInput v-for="(skill, index) in skills" :key="index" :skill="skill" />
        <div style="padding: 1.5rem;" class="flex-Center">
            <p class="miniURL" style="font-size: 0.80rem;" @click.stop="addSkill">
                Add skill
            </p>
        </div>
    </div>
</template>

<style scoped>
.skillSectionContainer {
    padding-left: 5%;
    overflow-y: auto;
    max-height: 20%;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
    margin-bottom: 1.5rem;

}

.miniURL {
    text-decoration: underline;
    color: #0839a3 !important;
    cursor: pointer;
}

.sectionHeaderInput {
    cursor: pointer;
    outline: none; /* Remove the default outline when focused */
}


</style>