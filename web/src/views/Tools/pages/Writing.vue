<template>
    <div class="writing">
        <h1>写一个字</h1>
        <div class="writing-canvas">
            <canvas id="canvas">您的浏览器不支持canvas</canvas>

            <div class="writing-controller">
                <div class="controller-label">
                    <label @click="colorLableClick(7, 'black')" :class="{ colorlabelselected: colorLabelSelectedIndex === 7 }" style="background-color:black;"></label>
                    <label @click="colorLableClick(0, 'red')" :class="{ colorlabelselected: colorLabelSelectedIndex === 0 }" style="background-color:red;"></label>
                    <label @click="colorLableClick(1, 'orange')" :class="{ colorlabelselected: colorLabelSelectedIndex === 1 }" style="background-color:orange;"></label>
                    <label @click="colorLableClick(2, 'yellow')" :class="{ colorlabelselected: colorLabelSelectedIndex === 2 }" style="background-color:yellow;"></label>
                    <label @click="colorLableClick(3, 'green')" :class="{ colorlabelselected: colorLabelSelectedIndex === 3 }" style="background-color:green;"></label>
                    <label @click="colorLableClick(4, 'blue')" :class="{ colorlabelselected: colorLabelSelectedIndex === 4 }" style="background-color:blue;"></label>
                    <label @click="colorLableClick(5, 'indigo')" :class="{ colorlabelselected: colorLabelSelectedIndex === 5 }" style="background-color:indigo;"></label>
                    <label @click="colorLableClick(6, 'purple')" :class="{ colorlabelselected: colorLabelSelectedIndex === 6 }" style="background-color:purple;"></label>
                </div>

                <div class="color-select">
                    <a-input @change="colorChange" v-model:value="selectedColor" type="color" placeholder="Basic usage" />
                    <label></label>
                </div>

                <div class="controller-btn">
                    <a-button @click="resetClick" type="primary">重置</a-button>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, reactive, toRefs } from 'vue';
export default defineComponent ({
    name: "Writing",

    setup(){
        let state = reactive({
            colorLabelSelectedIndex: 7,
            selectedColor: '#000000'
        })
        const canvasPropery = {
            canvasWidth: Math.min( 400, 900),
            canvasHeight: Math.min( 400, 900),
            strokeColor: 'black',
            isMouseDown: false,
            lastLoc: {x: 0, y: 0},
            lastTimestamp: 0,
            lastLineWidth: -1,
        };
        let canvas: any, context: any;

        onMounted(() => {
            canvas = document.getElementById("canvas") as HTMLCanvasElement;
            context = canvas.getContext("2d");

            canvas.width = canvasPropery.canvasWidth;
            canvas.height = canvasPropery.canvasWidth;

            drawGrid();

            canvas.addEventListener('mousedown', (e: MouseEvent) => {
                console.log(e);
                beginStroke({x: e.clientX , y: e.clientY});
            }, false);

            canvas.addEventListener('mouseup', () => {
                endStroke();
            }, false);

            canvas.addEventListener('mouseout', () => {
                endStroke();
            }, false);

            canvas.addEventListener('mousemove', (e: MouseEvent) => {
                if(canvasPropery.isMouseDown){
                    moveStroke({x: e.clientX , y: e.clientY})
                }
            }, false);
        })

        const drawGrid = () => {
            context.save()

            context.strokeStyle = "rgb(230,11,9)"

            context.beginPath()
            context.moveTo( 3 , 3 )
            context.lineTo( canvasPropery.canvasWidth - 3 , 3 )
            context.lineTo( canvasPropery.canvasWidth - 3 , canvasPropery.canvasHeight - 3 )
            context.lineTo( 3 , canvasPropery.canvasHeight - 3 )
            context.closePath()
            context.lineWidth = 6
            context.stroke()

            context.beginPath()
            context.moveTo(0,0)
            context.lineTo(canvasPropery.canvasWidth, canvasPropery.canvasHeight)

            context.moveTo(canvasPropery.canvasWidth, 0)
            context.lineTo(0, canvasPropery.canvasHeight)

            context.moveTo(canvasPropery.canvasWidth / 2, 0)
            context.lineTo(canvasPropery.canvasWidth / 2, canvasPropery.canvasHeight)

            context.moveTo(0, canvasPropery.canvasHeight / 2)
            context.lineTo(canvasPropery.canvasWidth, canvasPropery.canvasHeight / 2)

            context.lineWidth = 1
            context.stroke()

            context.restore()
        };
        const beginStroke = (point: {x: number, y: number}) => {
            canvasPropery.isMouseDown = true;
            canvasPropery.lastLoc = windowToCanvas(point.x, point.y);
            canvasPropery.lastTimestamp = new Date().getTime();
        };
        const moveStroke = (point: {x: number, y: number}) => {

            var curLoc = windowToCanvas( point.x , point.y );
            var curTimestamp = new Date().getTime();
            var s = calcDistance( curLoc , canvasPropery.lastLoc )
            var t = curTimestamp - canvasPropery.lastTimestamp

            var lineWidth = calcLineWidth( t , s );

            //draw
            context.beginPath();
            context.moveTo(canvasPropery.lastLoc.x , canvasPropery.lastLoc.y);
            context.lineTo(curLoc.x , curLoc.y);

            context.strokeStyle = canvasPropery.strokeColor
            context.lineWidth = lineWidth
            context.lineCap = "round"
            context.lineJoin = "round"
            context.stroke()

            canvasPropery.lastLoc = curLoc
            canvasPropery.lastTimestamp = curTimestamp
            canvasPropery.lastLineWidth = lineWidth
        }
        const endStroke = () => {
            canvasPropery.isMouseDown = false;
        }

        const windowToCanvas = (x: number , y: number) => {
            let bbox = canvas.getBoundingClientRect()
            return {x: Math.round(x - bbox.left) , y: Math.round(y - bbox.top)}
        };
        const calcDistance = (loc1: any, loc2: any) => {
            return Math.sqrt( (loc1.x - loc2.x)*(loc1.x - loc2.x) + (loc1.y - loc2.y)*(loc1.y - loc2.y) )
        };

        let maxLineWidth = 15;
        let minLineWidth = 1;
        let maxStrokeV = 10;
        let minStrokeV = 0.1;
        const calcLineWidth = (t: number, s: number) => {
            let v = s / t;

            let resultLineWidth;
            if(v <= minStrokeV)
                resultLineWidth = maxLineWidth;
            else if (v >= maxStrokeV)
                resultLineWidth = minLineWidth;
            else{
                resultLineWidth = maxLineWidth - (v-minStrokeV)/(maxStrokeV-minStrokeV)*(maxLineWidth-minLineWidth);
            }

            if( canvasPropery.lastLineWidth == -1 )
                return resultLineWidth;

            return resultLineWidth*1/3 + canvasPropery.lastLineWidth*2/3;
        };

        const colorLableClick = (index: number, color: string) => {
            state.colorLabelSelectedIndex = index;
            canvasPropery.strokeColor = color;
        }
        const colorChange = () => {
            console.log(state.selectedColor);
            canvasPropery.strokeColor = state.selectedColor;
        }

        const resetClick = () => {
            context.clearRect( 0 , 0 , canvasPropery.canvasWidth, canvasPropery.canvasHeight )
            drawGrid()
        }

        return {
            ...toRefs(state),
            colorLableClick,
            colorChange,
            resetClick
        }
    }
})
</script>

<style lang="scss">
.writing{
    display: flex;
    flex-direction: column;
    .writing-canvas{
        display: flex;
        .writing-controller{
            .controller-label{
                display: flex;
                .colorlabelselected{
                    border: 2px solid blueviolet;
                }
                label{
                    border:2px solid #fff;
                }
            }
            .color-select{
                display: flex;
                align-items: center;
                margin: 20px 0;

                label{
                    margin-left: 16px;
                }
                
            }
            .controller-btn{
                display: flex;
                justify-content: flex-end;
            }
            label{
                width: 25px;
                height: 25px;
                // margin: 10px 10px 0 0;
                border:2px solid #ccc;
                border-radius: 2px 2px;
                cursor: pointer;
            }
        }

        #canvas{
            cursor: pointer;
            margin-right: 16px;
        }
    }
    
}
</style>