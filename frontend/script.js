const API = "http://127.0.0.1:8080"

let cy

async function loadGraph(){

const res = await fetch(`${API}/api/workflows/1/graph`)
const data = await res.json()

data.sort((a,b)=>a.step-b.step)

let elements=[]

data.forEach((step,index)=>{
elements.push({
data:{
id:"step"+step.step,
label:`Step ${step.step}\n${step.connector}`
}
})

if(index>0){
elements.push({
data:{
source:"step"+data[index-1].step,
target:"step"+step.step
}
})
}
})

cy = cytoscape({
container:document.getElementById('workflowGraph'),

elements:elements,

style:[
{
selector:'node',
style:{
'label':'data(label)',
'background-color':'#3b82f6',
'color':'white',
'text-valign':'center',
'text-halign':'center',
'width':150,
'height':70
}
},
{
selector:'edge',
style:{
'width':3,
'line-color':'#94a3b8',
'target-arrow-color':'#94a3b8',
'target-arrow-shape':'triangle'
}
},
{
selector:'.success',
style:{'background-color':'#22c55e'}
},
{
selector:'.failed',
style:{'background-color':'#ef4444'}
}
],

layout:{
name:'breadthfirst',
directed:true,
spacingFactor:1.8,
padding:80
}
})

cy.fit()
cy.center()
}

async function executeWorkflow(){

document.getElementById("status").innerHTML="Running..."

await fetch(`${API}/api/workflows/1/execute`,{method:"POST"})

animateExecution()
}

async function animateExecution(){

const res = await fetch(`${API}/api/workflows/1/logs`)
const logs = await res.json()

logs.sort((a,b)=>a.stepOrder-b.stepOrder)

for(const log of logs){

let node = cy.getElementById("step"+log.stepOrder)

await new Promise(r=>setTimeout(r,800))

if(log.status==="SUCCESS"){
node.addClass('success')
}
else{
node.addClass('failed')
break
}
}
}

window.onload = loadGraph