import * as React from 'react';
import { Menubar, MenubarItem, MenubarMenu, MenubarTrigger } from '../ui/menubar';

export default function navbar() {
    return (
        <div className="flex justify-center">
            <Menubar className="mt-8 w-[450px] flex justify-center">
                <MenubarMenu className="flex py-10">
                    <MenubarTrigger>Home</MenubarTrigger>
                    <MenubarTrigger>About</MenubarTrigger>
                    <MenubarTrigger>About</MenubarTrigger>
                    <MenubarTrigger>About</MenubarTrigger>
                </MenubarMenu>
            </Menubar>
        </div>
    )
}