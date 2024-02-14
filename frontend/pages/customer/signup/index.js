import React, { useState } from 'react'
import Step1 from './_steps/step1'
import { Carousel, CarouselContent, CarouselItem } from '@/components/ui/carousel'

const signup = () => {
    

    return (
        <div className="flex justify-center mt-40">
            <Carousel>
                <CarouselContent>
                    <CarouselItem>
                        <Step1 />
                    </CarouselItem>
                    <CarouselItem></CarouselItem>
                </CarouselContent>
            </Carousel>
        </div>
    )
}

export default signup
