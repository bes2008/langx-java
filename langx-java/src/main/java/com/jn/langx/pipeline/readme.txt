langx 中提供了3种 多handler处理模型： chain, pipeline, event dispatcher
其中： chain, pipeline 都是可以用于双向，或者单向处理的， （对这个的理解，可以参考 agileway-eipmessage 中的readme）
又因为 双向处理本身就可以直接适配实现成单向处理。所以并没有提供单向处理的逻辑。