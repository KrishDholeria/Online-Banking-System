import React, { useState } from 'react'
import Step1 from './_steps/step1'
import Step2 from './_steps/step2'
import Step3 from './_steps/step3'
import { Carousel, CarouselContent, CarouselItem } from '@/components/ui/carousel'
import LoginNavbar from '@/components/navbar/loginNavbar'

const ForgotPassowrd = () => {
    const [customer, setCustomer] = useState();
    // setCustomer(JSON.parse(localStorage.getItem('customer')));


    return (
        <div>
            <LoginNavbar login={false}/>
            <div className="flex justify-center mt-32">
                <Carousel className="w-full md:w-[60%] lg:w-[40%] xl:w-[30%]">
                    <CarouselContent>
                        <CarouselItem>
                            <Step1 setUser={(value) => {
                                console.log("step1: "+value);
                                setCustomer(value);
                            }} />
                        </CarouselItem>
                        <CarouselItem>
                            <Step2 />
                        </CarouselItem>
                        <CarouselItem>
                            <Step3 user={customer} setUser={(value) => {
                                setCustomer(value);
                            }} />
                        </CarouselItem>
                    </CarouselContent>
                </Carousel>
            </div>
        </div>
    )
}

export default ForgotPassowrd
