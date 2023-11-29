<script setup>
import { ref, toRefs } from 'vue';

const levels = [
    "novice",
    "intermediate",
    "advanced",
    "expert",
]
const props = defineProps({
    skill: Object,
});

const { skill, level } = toRefs(props.skill);




</script>

<template>
    <div class="skillListContainer" style="display: flex; justify-content: space-between; width: 45%;">
        <div><label class="skill_label" for="skill_name">skill</label>
            <input type="text" id="skill_name" v-model="skill" required />
        </div>

        <div><label class="skill_label" for="skill_level">level</label>
            <select style="text-transform: capitalize;" v-model="level">
                <option style="text-transform: capitalize;" v-for="aLevel in levels" :key="aLevel" :value="aLevel">
                    {{ aLevel }}
                </option>
            </select>
        </div>

    </div>
</template>

<style scoped>
.skill_label {
    padding-right: 1.5rem;
}

.skillListContainer{
    overflow-y: auto;
    max-height: 10%;
    padding-left: 2%;
}
</style>