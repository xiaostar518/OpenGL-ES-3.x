//
//  ViewController.m
//  Sample14_4
//
//  Created by ldtxinkai on 15/9/21.
//  Copyright © 2015年 百纳. All rights reserved.
//

#import "ViewController.h"
#import "GLView.h"

@interface ViewController ()

@property (nonatomic, strong) GLView *glView;
@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


- (void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self glView];
}

- (GLView *)glView{
    if (!_glView) {
        _glView=[[GLView alloc] initWithFrame:self.view.bounds];
        [self.view addSubview:_glView];
    }
    return _glView;
}
@end
